package com.e2s.cam.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.e2s.cam.Error.UserNotFoundException;
import com.e2s.cam.entity.CommunityUserProfiles;
import com.e2s.cam.entity.GroupEntity;
import com.e2s.cam.entity.Staff;
import com.e2s.cam.entity.UserEntity;
import com.e2s.cam.repository.CommunityUserProfilesRepository;
import com.e2s.cam.repository.GroupRepository;
import com.e2s.cam.repository.StaffRepository;
import com.e2s.cam.repository.UserRepository;
import com.e2s.cam.response.Emails;
import com.e2s.cam.response.Groups;
import com.e2s.cam.response.ListResponse;
import com.e2s.cam.response.Meta;
import com.e2s.cam.response.Name;
import com.e2s.cam.response.User;
import com.e2s.cam.userentity.Users;
import com.e2s.cam.usersRepository.UsersRepository;
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private StaffRepository staffRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private CommunityUserProfilesRepository communityUserProfilesRepository;

	public User getByStaffId(String staffId) {
		UserEntity userDetails = userRepository.findByStaffId(staffId);
		if (Objects.equals(userDetails, null)) {
			throw new UserNotFoundException("Invalid User ID. Please check and try again.");
		}
		return constructResponse(userDetails);

	}

	public ListResponse getAllUsers(String filter, int startIndex, int itemsPerPage, String ascOrderBy,
			String descOrderBy) {
		Pageable pageable = constructPageable(startIndex, itemsPerPage, ascOrderBy, descOrderBy);
		Specification<UserEntity> spec = Specification.where(null);

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
        	System.out.println("hsdhsd "+filterParts.length);
            if (filterParts.length == 2) {
                String field = filterParts[0];
            	System.out.println("field1 "+field);
                String value = filterParts[1].replaceAll("'", ""); // Remove single quotes from the value
            	System.out.println("value "+value);
            	System.out.println("field "+field);
            	if(Objects.equals(field, "userId")) {
            		field="staffId";
            	}
                String finalField=field;

                switch (operator) {
                    case "co":
                        spec = spec.and((root, query, cb) -> cb.like(root.get(finalField), "%" + value + "%"));
                        break;
                    case "eq":
                        spec = spec.and((root, query, cb) -> cb.equal(root.get(finalField), value));
                        break;
                }
            }
        }
        ListResponse listOfUserResponse = new ListResponse();
		Page<UserEntity> userDetails = userRepository.findAll(spec,pageable);
		List<String> schema = new ArrayList<>();
		schema.add("urn:ietf:params:scim:api:messages:2.0:ListResponse");
		List<User> allUserDetails = new ArrayList<>();
		if (!Objects.equals(userDetails, null) && userDetails.getSize() > 0) {
			for (UserEntity user : userDetails.getContent()) {

				User constructedResponse = constructResponse(user);
				allUserDetails.add(constructedResponse);

			}
		}
		listOfUserResponse.setResource(allUserDetails);
		listOfUserResponse.setStartIndex(userDetails.getNumber());
		listOfUserResponse.setTotalResults(userDetails.getTotalElements());
		listOfUserResponse.setItemsPerPage(userDetails.getSize());
		return listOfUserResponse;
	}

	public User updateByStaffId(String staffId, String Status) {
		System.out.print("status "+Status);
		UserEntity userDetails = userRepository.findByStaffId(staffId);
		if (Objects.equals(userDetails, null)) {
			throw new UserNotFoundException("Invalid User ID. Please check and try again.");
		}
		Long id=Long.valueOf(userDetails.getUserId());
		Staff staff = staffRepository.findByStaffId(staffId);
		CommunityUserProfiles communityUserProfiles = communityUserProfilesRepository.findByUserId(userDetails.getUserId());
		Users authuser=usersRepository.findByIdAndOrganizationId(id, 76l);
		if (!Objects.equals(getStatus(Status), null)) {
			staff.setStatus(getStatus(Status));
			communityUserProfiles.setStatus(getStatus(Status));
			authuser.setStatus(getStatus(Status));
		}
		staffRepository.save(staff);
		communityUserProfilesRepository.save(communityUserProfiles);
		usersRepository.save(authuser);
		return constructResponse(userDetails);

	}

	public User constructResponse(UserEntity userDetails) {
		String userId = userDetails.getUserId();
		List<GroupEntity> groupDetails = groupRepository.findAllByUserId(userId);
		List<Groups> groups = new ArrayList<>();
		for (GroupEntity group : groupDetails) {
			Groups groupDetail = new Groups();
			groupDetail.setDisplay(group.getName());
			groupDetail.setValue(group.getId().toString());
			groupDetail.setSref("");
			groups.add(groupDetail);
		}
		List<Emails> emails = new ArrayList<>();
		Emails email = new Emails();
		email.setPrimary(true);
		email.setType("work");
		email.setValue(userDetails.getUserName());
		emails.add(email);
		System.out.println("user completed");
		User response = new User();
		response.setId(userDetails.getStaffId());
		response.setExternalId("");
		String createdAt =null;
		String updatedAt =null;
		try {
			 createdAt = changeData(userDetails.getCreatedAt());
			 updatedAt = changeData(userDetails.getUpdatedAt());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		Meta meta = new Meta();
		try {
			meta.setCreated(createdAt.toString());
			meta.setLastModified(updatedAt.toString());
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		meta.setResourceType("User");
		response.setMeta(meta);
		response.setUserName(userDetails.getUserName());
		response.setDisplayName(userDetails.getDisplayName());
		Name names = new Name(userDetails.getDisplayName(), userDetails.getDisplayName(), userDetails.getDisplayName());
		response.setName(names);
		if(Objects.equals(userDetails.getStatus(),"active")) {
			response.setActive(true);
		}else {
			response.setActive(false);
		}
		response.setEmails(emails);
		response.setProfileUrl( userDetails.getProfilePicUrl());
		response.setTitle( userDetails.getTitle());
		response.setUserType(userDetails.getUserType());
		response.setGroups(groups);

		// below code just hardcoded . need to change
//		UserExtension login = new UserExtension();
//		login.setIsPrivileged(null);
//		login.setLastLogin(null);
//		login.setLastPasswordChanged(null);
//		response.setExtension(login);
		System.out.println("response completed " + response);

		return response;
	}

	public Pageable constructPageable(int startIndex, int itemsPerPage, String ascOrderBy, String descOrderBy) {
		Sort sort = null;
		if (ascOrderBy != null) {
			sort = Sort.by(ascOrderBy).ascending();
		} else if (descOrderBy != null) {
			sort = Sort.by(descOrderBy).descending();
		}

		if (sort == null) {
			sort = Sort.by("id").ascending(); // You can change the field as per your requirement
		}

		return PageRequest.of(startIndex, itemsPerPage, sort);
	}

	String getStatus(String status) {
		if (Objects.equals(status, "true")) {
			return "active";

		} else if (Objects.equals(status, "false")) {
			return "inactive";
		}
		return null;
	}
	
	public void deleteUser(String staffId) {
		Staff staff=staffRepository.findByStaffId(staffId);
		if (Objects.equals(staff, null)) {
			throw new UserNotFoundException("Invalid User ID. Please check and try again.");
		}
		staffRepository.delete(staff);
		CommunityUserProfiles communityUserProfiles=communityUserProfilesRepository.findByUserId(staff.getUserId());
		communityUserProfilesRepository.delete(communityUserProfiles);
		Users authuser=usersRepository.findByIdAndOrganizationId(Long.valueOf(staff.getUserId()), 76l);
		usersRepository.delete(authuser);
		
	}
	
	public String changeData(Long time) {
		long currentMillis = System.currentTimeMillis();

        // Create a Date object from the milliseconds
        Date date = new Date(currentMillis);

        // Create a SimpleDateFormat object with the desired ISO 8601 format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        
        // Set the time zone to UTC
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Format the Date object as an ISO 8601 string
        String iso8601Format = sdf.format(date);
        return iso8601Format;
	}

	
}
