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
package de.symeda.sormas.api.report;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import de.symeda.sormas.api.facility.FacilityReferenceDto;
import de.symeda.sormas.api.region.DistrictReferenceDto;
import de.symeda.sormas.api.region.RegionReferenceDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.utils.EpiWeek;

@Remote
public interface WeeklyReportFacade {

	List<WeeklyReportDto> getAllWeeklyReportsAfter(Date date, String userUuid);
	
	List<WeeklyReportDto> getByUuids(List<String> uuids);
	
	WeeklyReportDto saveWeeklyReport(WeeklyReportDto dto);
	
	List<String> getAllUuids(String userUuid);
	
	WeeklyReportSummaryDto getSummaryDtoByRegion(RegionReferenceDto regionRef, EpiWeek epiWeek);
	
	WeeklyReportSummaryDto getSummaryDtoByDistrict(DistrictReferenceDto districtRef, EpiWeek epiWeek);
	
	int getNumberOfWeeklyReportsByFacility(FacilityReferenceDto facilityRef, EpiWeek epiWeek);
	
	List<WeeklyReportReferenceDto> getWeeklyReportsByFacility(FacilityReferenceDto facilityRef, EpiWeek epiWeek);
	
	List<WeeklyReportSummaryDto> getSummariesPerRegion(EpiWeek epiWeek);
	
	List<WeeklyReportSummaryDto> getSummariesPerDistrict(RegionReferenceDto region, EpiWeek epiWeek);
	
	WeeklyReportReferenceDto getByEpiWeekAndUser(EpiWeek epiWeek, UserReferenceDto userRef);
	
	WeeklyReportDto getByUuid(String uuid);
	
}
