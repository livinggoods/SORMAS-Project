/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.api.user;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import de.symeda.sormas.api.facility.FacilityReferenceDto;
import de.symeda.sormas.api.region.DistrictReferenceDto;
import de.symeda.sormas.api.region.RegionReferenceDto;

@Remote
public interface UserFacade {

    List<UserDto> getAll(UserRole... roles);
    
    UserDto getByUuid(String uuid);
    
    UserDto saveUser(UserDto dto);

    boolean isLoginUnique(String uuid, String userName);
    
    String resetPassword(String uuid);

	List<UserDto> getAllAfter(Date date);

	UserDto getByUserName(String userName);

	UserReferenceDto getByUserNameAsReference(String userName);

	List<UserReferenceDto> getAllAfterAsReference(Date date);

	List<UserReferenceDto> getUsersByRegionAndRoles(RegionReferenceDto regionRef, UserRole... assignableRoles);
	
	/**
	 * 
	 * @param district
	 * @param includeSupervisors independent from the district
	 * @param assignableRoles roles of the users by district
	 * @return
	 */
	List<UserReferenceDto> getAssignableUsersByDistrict(DistrictReferenceDto district, boolean includeSupervisors, UserRole... assignableRoles);

	int getNumberOfInformantsByFacility(FacilityReferenceDto facilityRef);
	
	List<UserReferenceDto> getForWeeklyReportDetails(DistrictReferenceDto districtRef);

	List<String> getAllUuids(String userUuid);

	List<UserDto> getByUuids(List<String> uuids);
	
	UserDto getCurrentUser();
	
	UserReferenceDto getCurrentUserAsReference();
	
}
