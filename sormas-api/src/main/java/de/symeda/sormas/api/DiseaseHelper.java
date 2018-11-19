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
package de.symeda.sormas.api;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import de.symeda.sormas.api.caze.PlagueType;
import de.symeda.sormas.api.symptoms.SymptomState;
import de.symeda.sormas.api.symptoms.SymptomsDto;
import de.symeda.sormas.api.utils.DataHelper;

public class DiseaseHelper {

	/**
	 * Checks whether the given symptoms match the clinical criteria of one of the three Plague types.
	 * 
	 * @param symptoms The symptoms of a case with the Plague disease
	 * @return One of the three Plague types if the clinical criteria are met, null otherwise
	 */
	public static PlagueType getPlagueTypeForSymptoms(SymptomsDto symptoms) {
		if (symptoms.getFever() == SymptomState.YES) {
			if (symptoms.getPainfulLymphadenitis() == SymptomState.YES) {
				return PlagueType.BUBONIC;
			} else if (symptoms.getCough() == SymptomState.YES || symptoms.getChestPain() == SymptomState.YES ||
					symptoms.getCoughingBlood() == SymptomState.YES) {
				return PlagueType.PNEUMONIC;
			} else if (symptoms.getChillsSweats() == SymptomState.YES) {
				return PlagueType.SEPTICAEMIC;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static boolean hasContactFollowUp(Disease disease, PlagueType plagueType) {
		return disease == Disease.EVD || disease == Disease.LASSA || disease == Disease.NEW_INFLUENCA 
				|| disease == Disease.MONKEYPOX || (disease == Disease.PLAGUE) || disease == Disease.OTHER;
	}
	
	public static List<Disease> getAllDiseasesWithFollowUp() {
		EnumSet<Disease> diseases = EnumSet.allOf(Disease.class);
		List<Disease> diseasesWithFollowUp = new ArrayList<>();
		
		for (Disease disease : diseases) {
			if (hasContactFollowUp(disease, null)) {
				diseasesWithFollowUp.add(disease);
			}
		}
		
		return diseasesWithFollowUp;
	}

	public static int getIncubationPeriodDays(Disease disease, PlagueType plagueType) {

		if (disease == null) {
			return 21; // max
		}

		switch(disease) {
		case EVD:
		case MEASLES:
		case MONKEYPOX:
		case LASSA:
		case OTHER:
			return 21;
		case NEW_INFLUENCA:
			return 17;
		case DENGUE:
			return 14;
		case CSM:
			return 10;
		case PLAGUE:
			return 7;
		case YELLOW_FEVER:
			return 6;
		case CHOLERA:
			return 5;
		default:
			return 21; // max
		}
	}
	
	public static String toString(Disease disease, String diseaseDetails) {
		return disease != Disease.OTHER 
				? (disease != null ? disease.toShortString() : "")
				: DataHelper.toStringNullable(diseaseDetails);
	}
}
