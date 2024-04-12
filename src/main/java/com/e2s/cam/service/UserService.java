package com.e2s.cam.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.e2s.cam.Error.UserNotFoundException;
import com.e2s.cam.entity.CommunityUserProfiles;
import com.e2s.cam.entity.GroupDetails;
import com.e2s.cam.entity.Staff;
import com.e2s.cam.entity.UserEntity;
import com.e2s.cam.repository.CommunityUserProfilesRepository;
import com.e2s.cam.repository.GroupDetailsRepository;
import com.e2s.cam.repository.GroupRepository;
import com.e2s.cam.repository.StaffRepository;
import com.e2s.cam.repository.UserRepository;
import com.e2s.cam.repository.UserRolesRepository;
import com.e2s.cam.request.Request;
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
	private GroupDetailsRepository groupDetailsRepository;

	@Autowired
	private StaffRepository staffRepository;

	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private UserRolesRepository userRolesRepository;

	@Autowired
	private CommunityUserProfilesRepository communityUserProfilesRepository;
	
	
	@Value("${SCIM.API.Org}")
	Long orgId;

	public User getByStaffId(String staffId) {
		UserEntity userDetails = userRepository.findByStaffId(staffId);
		if (Objects.equals(userDetails, null)) {
			throw new UserNotFoundException("Invalid User ID. Please check and try again.");
		}
		return constructResponse(userDetails);

	}

	public ListResponse getAllUsers(Request request) {
		
//		Pageable pageable = null;
		Specification<UserEntity> spec = Specification.where(null);

        if (!Objects.equals(request, null)) {
        	String filter=request.getFilter();
//    		int startIndex=1; 
//    		int itemsPerPage=10;
//    		System.out.println("size"+request.getItemsPerPage());
//    		if(!Objects.equals(request.getItemsPerPage(), 0)) {
//    			startIndex=request.getStartIndex(); 
//           	 itemsPerPage= request.getItemsPerPage();
//    		}
//    		String ascOrderBy=request.getAscOrderBy();
//    		String descOrderBy=request.getDescOrderBy();
//    		pageable=constructPageable(startIndex, itemsPerPage, ascOrderBy, descOrderBy);
            if(!Objects.equals(filter, null)&&!Objects.equals(filter, "")) {
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
        }
//            else {
//			pageable=Pageable.unpaged();
//		}
        List<UserEntity> dteails=new ArrayList<>();
        Sort sort=constructSort(request);
        dteails=userRepository.findAll(spec,sort);
        int totalResults = dteails.size();
        int endIndex =0;
        int  startIndex=0; 
        int  itemsPerPage= 0;
        if(!Objects.equals(request, null)) {
        	  startIndex=request.getStartIndex(); 
              itemsPerPage= request.getItemsPerPage();
        	endIndex= Math.min(request.getStartIndex() + request.getItemsPerPage(), totalResults);
        	 dteails = dteails.subList(request.getStartIndex()  - 1, endIndex);
        }else {
        	startIndex=1;
        	itemsPerPage=totalResults;
        	
        }

          ListResponse listOfUserResponse = new ListResponse();
		List<String> schema = new ArrayList<>();
		schema.add("urn:ietf:params:scim:api:messages:2.0:ListResponse");
		List<User> allUserDetails = new ArrayList<>();
		if (!Objects.equals(dteails, null) && dteails.size() > 0) {
			for (UserEntity user : dteails) {

				User constructedResponse = constructResponse(user);
				allUserDetails.add(constructedResponse);

			}
		}
		listOfUserResponse.setResource(allUserDetails);
		listOfUserResponse.setStartIndex(startIndex);
		listOfUserResponse.setTotalResults(totalResults);
		listOfUserResponse.setItemsPerPage(itemsPerPage);
//        ListResponse listOfUserResponse = new ListResponse();
//		Page<UserEntity> userDetails = userRepository.findAll(spec,pageable);
//		List<String> schema = new ArrayList<>();
//		schema.add("urn:ietf:params:scim:api:messages:2.0:ListResponse");
//		List<User> allUserDetails = new ArrayList<>();
//		if (!Objects.equals(userDetails, null) && userDetails.getSize() > 0) {
//			for (UserEntity user : userDetails.getContent()) {
//
//				User constructedResponse = constructResponse(user);
//				allUserDetails.add(constructedResponse);
//
//			}
//		}
//		listOfUserResponse.setResource(allUserDetails);
//		listOfUserResponse.setStartIndex(userDetails.getNumber()+1);
//		listOfUserResponse.setTotalResults(userDetails.getTotalElements());
//		listOfUserResponse.setItemsPerPage(userDetails.getSize());
		return listOfUserResponse;
	}

	public User updateByStaffId(String staffId, String Status) {
		System.out.println("status "+Status);
		UserEntity userDetails = userRepository.findByStaffId(staffId);
		if (Objects.equals(userDetails, null)) {
			throw new UserNotFoundException("Invalid User ID. Please check and try again.");
		}
		Long id=Long.valueOf(userDetails.getUserId());
		System.out.println("userId "+Status);
		Staff staff = staffRepository.findByStaffId(staffId);
		System.out.println("staff "+staff);
		CommunityUserProfiles communityUserProfiles = communityUserProfilesRepository.findByUserId(userDetails.getUserId());
		System.out.println("CommunityUserProfiles "+staff);
		Users authuser=usersRepository.findByIdAndOrganizationId(id, orgId);
		if (!Objects.equals(getStatus(Status), null)) {
			staff.setStatus(getStatus(Status));
			communityUserProfiles.setStatus(getStatus(Status));
			authuser.setStatus(getStatus(Status));
		}
		System.out.println("CommunityUserProfiles "+staff);
		staffRepository.save(staff);
		System.out.println("staff saves "+staff);
		communityUserProfilesRepository.save(communityUserProfiles);
		System.out.println("comm saves "+staff);
		usersRepository.updateName(orgId,id,getStatus(Status));
		System.out.println("users saves "+staff);
		 if (Objects.equals(Status, "false")) {
			 userRolesRepository.deleteByUserId(userDetails.getUserId());
			}
		return constructResponse(userDetails);

	}

	public User constructResponse(UserEntity userDetails) {
		String userId = userDetails.getUserId();
		List<GroupDetails> groupDetails = groupDetailsRepository.findAllByUserId(userId);
		List<Groups> groups = new ArrayList<>();
		for (GroupDetails group : groupDetails) {
			Groups groupDetail = new Groups();
			System.out.println(group.getName());
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
			 createdAt = changeDateFormat(userDetails.getCreatedAt());
			 updatedAt = changeDateFormat(userDetails.getUpdatedAt());
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
		if (!Objects.equals(ascOrderBy, null)&&!Objects.equals(ascOrderBy, "")) {
			sort = Sort.by(ascOrderBy).ascending();
		} else if (!Objects.equals(descOrderBy, null)&&!Objects.equals(descOrderBy, "")) {
			sort = Sort.by(descOrderBy).descending();
		}

		if (sort == null) {
			sort = Sort.by("id").ascending(); // You can change the field as per your requirement
		}
		if(startIndex!=0) {
			startIndex=startIndex-1;
		}
		return PageRequest.of(startIndex, itemsPerPage, sort);
	}
	public Sort constructSort(Request request) {
		Sort sort = null;
		if(!Objects.equals(request, null)) {
			if (!Objects.equals(request.getAscOrderBy(), null)&&!Objects.equals(request.getAscOrderBy(), "")) {
				sort = Sort.by(request.getAscOrderBy()).ascending();
			} else if (!Objects.equals(request.getDescOrderBy(), null)&&!Objects.equals(request.getDescOrderBy(), "")) {
				sort = Sort.by(request.getDescOrderBy()).descending();
			}

			if (sort == null) {
				sort = Sort.by("id").ascending(); // You can change the field as per your requirement
			}
			
			return sort;
		}else {
			return Sort.by("id").ascending();
		}
	
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
		Users authuser=usersRepository.findByIdAndOrganizationId(Long.valueOf(staff.getUserId()), orgId);
		usersRepository.delete(authuser);
		
	}
	
	public String changeDateFormat(Long currentTimeMillis) {
		if(Objects.equals(currentTimeMillis, null)) {
			return "";
		}
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Set the time zone to UTC to match the format you provided

        // Convert milliseconds to Date object
        Date date = new Date(currentTimeMillis);

        // Format the date according to the specified format
        String formattedDate = dateFormat.format(date);

        // Print the formatted date
       return formattedDate;
    }
	
	
	
}
