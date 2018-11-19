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
package de.symeda.sormas.backend.epidata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.symeda.auditlog.api.Audited;
import de.symeda.sormas.api.epidata.AnimalCondition;
import de.symeda.sormas.api.epidata.WaterSource;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.ImportIgnore;

@Entity
@Audited
public class EpiData extends AbstractDomainObject {

	private static final long serialVersionUID = -8294812479501735785L;

	public static final String TABLE_NAME = "epidata";
	
	public static final String BURIAL_ATTENDED = "burialAttended";
	public static final String BURIALS = "burials";
	public static final String GATHERING_ATTENDED = "gatheringAttended";
	public static final String GATHERINGS = "gatherings";
	public static final String TRAVELED = "traveled";
	public static final String TRAVELS = "travels";
	public static final String RODENTS = "rodents";
	public static final String BATS = "bats";
	public static final String PRIMATES = "primates";
	public static final String SWINE = "swine";
	public static final String BIRDS = "birds";
	public static final String CATTLE = "cattle";
	public static final String OTHER_ANIMALS = "otherAnimals";
	public static final String OTHER_ANIMALS_DETAILS = "otherAnimalsDetails";
	public static final String WATER_SOURCE = "waterSource";
	public static final String WATER_SOURCE_OTHER = "waterSourceOther";
	public static final String WATER_BODY = "waterBody";
	public static final String WATER_BODY_DETAILS = "waterBodyDetails";
	public static final String TICKBITE = "tickBite";
	public static final String DATE_OF_LAST_EXPOSURE = "dateOfLastExposure";
	public static final String PLACE_OF_LAST_EXPOSURE = "placeOfLastExposure";
	public static final String ANIMAL_CONDITION = "animalCondition";
	public static final String DIRECT_CONTACT_CONFIRMED_CASE = "directContactConfirmedCase";
	
	private YesNoUnknown burialAttended;
	private YesNoUnknown gatheringAttended;
	private YesNoUnknown traveled;

	private Date changeDateOfEmbeddedLists;
	private List<EpiDataBurial> burials = new ArrayList<>();
	private List<EpiDataGathering> gatherings = new ArrayList<>();
	private List<EpiDataTravel> travels = new ArrayList<>();

	private YesNoUnknown directContactConfirmedCase;
	private YesNoUnknown directContactProbableCase;
	private YesNoUnknown closeContactProbableCase;
	private YesNoUnknown areaConfirmedCases;

	private YesNoUnknown processingConfirmedCaseFluidUnsafe;
	private YesNoUnknown percutaneousCaseBlood;
	private YesNoUnknown directContactDeadUnsafe;
	private YesNoUnknown processingSuspectedCaseSampleUnsafe;

	private YesNoUnknown areaInfectedAnimals;
	private YesNoUnknown sickDeadAnimals;
	private String sickDeadAnimalsDetails;
	private Date sickDeadAnimalsDate;
	private String sickDeadAnimalsLocation;
	private YesNoUnknown eatingRawAnimalsInInfectedArea;
	private YesNoUnknown eatingRawAnimals;
	private String eatingRawAnimalsDetails;
	
	private YesNoUnknown rodents;
	private YesNoUnknown bats;
	private YesNoUnknown primates;
	private YesNoUnknown swine;
	private YesNoUnknown birds;
	private YesNoUnknown cattle;
	private YesNoUnknown otherAnimals;
	private String otherAnimalsDetails;
	private WaterSource waterSource;
	private String waterSourceOther;
	private YesNoUnknown waterBody;
	private String waterBodyDetails;
	private YesNoUnknown tickBite;
	private YesNoUnknown fleaBite;
	private Date dateOfLastExposure;
	private String placeOfLastExposure;
	private AnimalCondition animalCondition;

	@ImportIgnore
	@Enumerated(EnumType.STRING)
	public YesNoUnknown getBurialAttended() {
		return burialAttended;
	}
	public void setBurialAttended(YesNoUnknown burialAttended) {
		this.burialAttended = burialAttended;
	}

	@ImportIgnore
	@Enumerated(EnumType.STRING)
	public YesNoUnknown getGatheringAttended() {
		return gatheringAttended;
	}
	public void setGatheringAttended(YesNoUnknown gatheringAttended) {
		this.gatheringAttended = gatheringAttended;
	}

	@ImportIgnore
	@Enumerated(EnumType.STRING)
	public YesNoUnknown getTraveled() {
		return traveled;
	}
	public void setTraveled(YesNoUnknown traveled) {
		this.traveled = traveled;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getRodents() {
		return rodents;
	}
	public void setRodents(YesNoUnknown rodents) {
		this.rodents = rodents;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getBats() {
		return bats;
	}
	public void setBats(YesNoUnknown bats) {
		this.bats = bats;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getPrimates() {
		return primates;
	}
	public void setPrimates(YesNoUnknown primates) {
		this.primates = primates;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getSwine() {
		return swine;
	}
	public void setSwine(YesNoUnknown swine) {
		this.swine = swine;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getBirds() {
		return birds;
	}
	public void setBirds(YesNoUnknown birds) {
		this.birds = birds;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getCattle() {
		return cattle;
	}
	public void setCattle(YesNoUnknown cattle) {
		this.cattle = cattle;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getOtherAnimals() {
		return otherAnimals;
	}
	public void setOtherAnimals(YesNoUnknown otherAnimals) {
		this.otherAnimals = otherAnimals;
	}

	@Column(length=512)
	public String getOtherAnimalsDetails() {
		return otherAnimalsDetails;
	}
	public void setOtherAnimalsDetails(String otherAnimalsDetails) {
		this.otherAnimalsDetails = otherAnimalsDetails;
	}

	@Enumerated(EnumType.STRING)
	public WaterSource getWaterSource() {
		return waterSource;
	}
	public void setWaterSource(WaterSource waterSource) {
		this.waterSource = waterSource;
	}

	@Column(length=512)
	public String getWaterSourceOther() {
		return waterSourceOther;
	}
	public void setWaterSourceOther(String waterSourceOther) {
		this.waterSourceOther = waterSourceOther;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getWaterBody() {
		return waterBody;
	}
	public void setWaterBody(YesNoUnknown waterBody) {
		this.waterBody = waterBody;
	}

	@Column(length=512)
	public String getWaterBodyDetails() {
		return waterBodyDetails;
	}
	public void setWaterBodyDetails(String waterBodyDetails) {
		this.waterBodyDetails = waterBodyDetails;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getTickBite() {
		return tickBite;
	}
	public void setTickBite(YesNoUnknown tickBite) {
		this.tickBite = tickBite;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getFleaBite() {
		return fleaBite;
	}
	public void setFleaBite(YesNoUnknown fleaBite) {
		this.fleaBite = fleaBite;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateOfLastExposure() {
		return dateOfLastExposure;
	}
	public void setDateOfLastExposure(Date dateOfLastExposure) {
		this.dateOfLastExposure = dateOfLastExposure;
	}

	@Column(length=512)
	public String getPlaceOfLastExposure() {
		return placeOfLastExposure;
	}
	public void setPlaceOfLastExposure(String placeOfLastExposure) {
		this.placeOfLastExposure = placeOfLastExposure;
	}

	@Enumerated(EnumType.STRING)
	public AnimalCondition getAnimalCondition() {
		return animalCondition;
	}
	public void setAnimalCondition(AnimalCondition animalCondition) {
		this.animalCondition = animalCondition;
	}
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = EpiDataBurial.EPI_DATA)
	public List<EpiDataBurial> getBurials() {
		return burials;
	}
	public void setBurials(List<EpiDataBurial> burials) {
		this.burials = burials;
	}
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = EpiDataGathering.EPI_DATA)
	public List<EpiDataGathering> getGatherings() {
		return gatherings;
	}
	public void setGatherings(List<EpiDataGathering> gatherings) {
		this.gatherings = gatherings;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = EpiDataTravel.EPI_DATA)
	public List<EpiDataTravel> getTravels() {
		return travels;
	}
	public void setTravels(List<EpiDataTravel> travels) {
		this.travels = travels;
	}
	
	/**
	 * This change date has to be set whenever one of the embedded lists is modified: !oldList.equals(newList)
	 * @return
	 */
	@ImportIgnore
	public Date getChangeDateOfEmbeddedLists() {
		return changeDateOfEmbeddedLists;
	}
	public void setChangeDateOfEmbeddedLists(Date changeDateOfEmbeddedLists) {
		this.changeDateOfEmbeddedLists = changeDateOfEmbeddedLists;
	}

	@Enumerated(EnumType.STRING)	
	public YesNoUnknown getDirectContactConfirmedCase() {
		return directContactConfirmedCase;
	}
	public void setDirectContactConfirmedCase(YesNoUnknown directContactConfirmedCase) {
		this.directContactConfirmedCase = directContactConfirmedCase;
	}
	@Enumerated(EnumType.STRING)
	public YesNoUnknown getDirectContactProbableCase() {
		return directContactProbableCase;
	}
	public void setDirectContactProbableCase(YesNoUnknown directContactProbableCase) {
		this.directContactProbableCase = directContactProbableCase;
	}
	@Enumerated(EnumType.STRING)
	public YesNoUnknown getCloseContactProbableCase() {
		return closeContactProbableCase;
	}
	public void setCloseContactProbableCase(YesNoUnknown closeContactProbableCase) {
		this.closeContactProbableCase = closeContactProbableCase;
	}
	@Enumerated(EnumType.STRING)
	public YesNoUnknown getAreaConfirmedCases() {
		return areaConfirmedCases;
	}
	public void setAreaConfirmedCases(YesNoUnknown areaConfirmedCases) {
		this.areaConfirmedCases = areaConfirmedCases;
	}
	@Enumerated(EnumType.STRING)
	public YesNoUnknown getProcessingConfirmedCaseFluidUnsafe() {
		return processingConfirmedCaseFluidUnsafe;
	}
	public void setProcessingConfirmedCaseFluidUnsafe(YesNoUnknown processingConfirmedCaseFluidUnsafe) {
		this.processingConfirmedCaseFluidUnsafe = processingConfirmedCaseFluidUnsafe;
	}
	@Enumerated(EnumType.STRING)
	public YesNoUnknown getPercutaneousCaseBlood() {
		return percutaneousCaseBlood;
	}
	public void setPercutaneousCaseBlood(YesNoUnknown percutaneousCaseBlood) {
		this.percutaneousCaseBlood = percutaneousCaseBlood;
	}
	@Enumerated(EnumType.STRING)
	public YesNoUnknown getDirectContactDeadUnsafe() {
		return directContactDeadUnsafe;
	}
	public void setDirectContactDeadUnsafe(YesNoUnknown directContactDeadUnsafe) {
		this.directContactDeadUnsafe = directContactDeadUnsafe;
	}
	@Enumerated(EnumType.STRING)
	public YesNoUnknown getProcessingSuspectedCaseSampleUnsafe() {
		return processingSuspectedCaseSampleUnsafe;
	}
	public void setProcessingSuspectedCaseSampleUnsafe(YesNoUnknown processingSuspectedCaseSampleUnsafe) {
		this.processingSuspectedCaseSampleUnsafe = processingSuspectedCaseSampleUnsafe;
	}
	@Enumerated(EnumType.STRING)
	public YesNoUnknown getAreaInfectedAnimals() {
		return areaInfectedAnimals;
	}
	public void setAreaInfectedAnimals(YesNoUnknown areaInfectedAnimals) {
		this.areaInfectedAnimals = areaInfectedAnimals;
	}
	@Enumerated(EnumType.STRING)
	public YesNoUnknown getSickDeadAnimals() {
		return sickDeadAnimals;
	}
	public void setSickDeadAnimals(YesNoUnknown sickDeadAnimals) {
		this.sickDeadAnimals = sickDeadAnimals;
	}
	@Column(length=512)
	public String getSickDeadAnimalsDetails() {
		return sickDeadAnimalsDetails;
	}
	public void setSickDeadAnimalsDetails(String sickDeadAnimalsDetails) {
		this.sickDeadAnimalsDetails = sickDeadAnimalsDetails;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getSickDeadAnimalsDate() {
		return sickDeadAnimalsDate;
	}
	public void setSickDeadAnimalsDate(Date sickDeadAnimalsDate) {
		this.sickDeadAnimalsDate = sickDeadAnimalsDate;
	}
	@Column(length=512)
	public String getSickDeadAnimalsLocation() {
		return sickDeadAnimalsLocation;
	}
	public void setSickDeadAnimalsLocation(String sickDeadAnimalsLocation) {
		this.sickDeadAnimalsLocation = sickDeadAnimalsLocation;
	}
	@Enumerated(EnumType.STRING)
	public YesNoUnknown getEatingRawAnimalsInInfectedArea() {
		return eatingRawAnimalsInInfectedArea;
	}
	public void setEatingRawAnimalsInInfectedArea(YesNoUnknown eatingRawAnimalsInInfectedArea) {
		this.eatingRawAnimalsInInfectedArea = eatingRawAnimalsInInfectedArea;
	}
	@Enumerated(EnumType.STRING)
	public YesNoUnknown getEatingRawAnimals() {
		return eatingRawAnimals;
	}
	public void setEatingRawAnimals(YesNoUnknown eatingRawAnimals) {
		this.eatingRawAnimals = eatingRawAnimals;
	}
	@Column(length=512)
	public String getEatingRawAnimalsDetails() {
		return eatingRawAnimalsDetails;
	}
	public void setEatingRawAnimalsDetails(String eatingRawAnimalsDetails) {
		this.eatingRawAnimalsDetails = eatingRawAnimalsDetails;
	}
	
}
