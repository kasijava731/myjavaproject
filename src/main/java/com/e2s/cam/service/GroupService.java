package com.e2s.cam.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.e2s.cam.Error.UserNotFoundException;
import com.e2s.cam.entity.GroupEntity;
import com.e2s.cam.entity.Roles;
import com.e2s.cam.entity.UserEntity;
import com.e2s.cam.entity.UserRoles;
import com.e2s.cam.repository.GroupRepository;
import com.e2s.cam.repository.RoleRepository;
import com.e2s.cam.repository.UserRepository;
import com.e2s.cam.repository.UserRolesRepository;
import com.e2s.cam.request.Operation;
import com.e2s.cam.response.GroupResponse;
import com.e2s.cam.response.ListResponse;
import com.e2s.cam.response.Member;
import com.e2s.cam.response.Meta;
import com.e2s.cam.usersRepository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GroupService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRolesRepository userRolesRepository;
	
	@Autowired
	private UserRepository userRepository;

	public GroupResponse getById(Long groupId) {
		Roles roleDetails = roleRepository.findByGroupId(groupId);
		if (Objects.equals(roleDetails, null)) {
			throw new UserNotFoundException("Invalid Group ID. Please check and try again.");
		}
		return constructResponse(roleDetails, false);

	}

	public ListResponse getAllGroups(String filter, int startIndex, int itemsPerPage, String ascOrderBy,
			String descOrderBy) {
		Specification<Roles> spec = Specification.where(null);

        if (filter != null) {
            String[] filterParts = null;
            String operator="";
        	if(filter.contains(" co ")) {
                filterParts = filter.split(" co ");
                operator="co";
        	}else if (filter.contains(" eq ")) {
        		filterParts = filter.split(" eq ");
                operator="eq";
        	}
            if (filterParts.length == 2) {
                String field1 = filterParts[0];
                String value = filterParts[1].replaceAll("'", ""); // Remove single quotes from the value
                String field=getFieldName(field1);
                switch (operator) {
                    case "co":
                        spec = spec.and((root, query, cb) -> cb.like(root.get(field), "%" + value + "%"));
                        break;
                    case "eq":
                        spec = spec.and((root, query, cb) -> cb.equal(root.get(field), value));
                        break;
                    // Add more operators as needed
                }
            }
        }
		Pageable pageable = userService.constructPageable(startIndex, itemsPerPage, ascOrderBy, descOrderBy);
		ListResponse listOfUserResponse = new ListResponse();
		Page<Roles> roleDetails = roleRepository.findAll(spec,pageable);
		List<String> schema = new ArrayList<>();
		schema.add("urn:ietf:params:scim:api:messages:2.0:ListResponse");
		List<GroupResponse> allUserDetails = new ArrayList<>();
		if (!Objects.equals(roleDetails, null) && roleDetails.getSize() > 0) {
			for (Roles user : roleDetails.getContent()) {

				GroupResponse constructedResponse = constructResponse(user, true);
				allUserDetails.add(constructedResponse);

			}
		}
		listOfUserResponse.setResource(allUserDetails);
		listOfUserResponse.setStartIndex(roleDetails.getNumber()+1);
		listOfUserResponse.setTotalResults(roleDetails.getTotalElements());
		listOfUserResponse.setItemsPerPage(roleDetails.getSize());
		return listOfUserResponse;
	}

	public GroupResponse constructResponse(Roles roles, boolean isGetAll) {
		Long id = roles.getGroupId();
		GroupResponse response = new GroupResponse();
		List<GroupEntity> groupDetails = groupRepository.findAllById(id);
		List<Member> members = new ArrayList<>();
		System.out.println(groupDetails);

		for (GroupEntity group : groupDetails) {
			Member member = new Member();
			member.setType("User");
			member.setValue(group.getStaffId());
			members.add(member);
			System.out.println(members);

		}

		response.setId(String.valueOf(id));
		Timestamp createdAt = new Timestamp(roles.getCreatedAt());
		Timestamp updatedAt = new Timestamp(roles.getUpdatedAt());
		Meta meta = new Meta();
		meta.setCreated(createdAt.toString());
		meta.setLastModified(updatedAt.toString());
		meta.setResourceType("Group");
		response.setMeta(meta);
		response.setDisplayName(roles.getGroupName());
		response.setMembers(members);
		return response;
	}

	public void updateById(String groupId, Operation operatios) {
		Long id = Long.valueOf(groupId);
		if (Objects.equals("members", operatios.getPath())) {
			if (Objects.equals(operatios.getOp(), "Add")) {
				UserRoles userRoles = new UserRoles();
				userRoles.setStatus("active");
				UserEntity userDetails= userRepository.findByStaffId(operatios.getValue());
				 userRoles.setUserId(userDetails.getUserId());
				userRoles.setCreatedAt(System.currentTimeMillis());
				userRoles.setUpdatedAt(System.currentTimeMillis());
				userRoles.setRoleId(Long.valueOf(groupId));
				userRoles.setUpdateBy(0l);
				userRoles.setCreatedBy(0l);
				userRolesRepository.save(userRoles);
			} else if (Objects.equals(operatios.getOp(), "Remove")) {
				UserEntity userDetails= userRepository.findByStaffId(operatios.getValue());
				UserRoles userRoles = userRolesRepository.findByUserIdAndRoleId(userDetails.getUserId(), id);
				userRolesRepository.delete(userRoles);
			}
		}

	}

	public void removeGroup(String groupId) {
		Long id = Long.valueOf(groupId);
		Optional<Roles> roleDetails = roleRepository.findById(id);
		if (roleDetails.isEmpty()) {
			throw new UserNotFoundException("Invalid Group ID. Please check and try again.");
		}
		List<UserRoles> userRoles = userRolesRepository.findAllByRoleId(id);
		roleRepository.delete(roleDetails.get());
		userRolesRepository.deleteAll(userRoles);
		

	}
	String getFieldName(String fieldName) {
		if(Objects.equals(fieldName, "groupName")) {
			return "name";
		}else if(Objects.equals(fieldName, "groupId")) {
			return "id";
		}
		return null;
	}
	
}
