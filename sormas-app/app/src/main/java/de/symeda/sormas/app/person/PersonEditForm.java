package de.symeda.sormas.app.person;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.I18nProperties;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.facility.FacilityDto;
import de.symeda.sormas.api.person.ApproximateAgeType;
import de.symeda.sormas.api.person.ApproximateAgeType.ApproximateAgeHelper;
import de.symeda.sormas.api.person.BurialConductor;
import de.symeda.sormas.api.person.CauseOfDeath;
import de.symeda.sormas.api.person.DeathPlaceType;
import de.symeda.sormas.api.person.OccupationType;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.PresentCondition;
import de.symeda.sormas.api.person.Sex;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.Diseases;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.caze.Case;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.facility.Facility;
import de.symeda.sormas.app.backend.location.Location;
import de.symeda.sormas.app.backend.person.Person;
import de.symeda.sormas.app.backend.person.PersonDao;
import de.symeda.sormas.app.backend.region.Community;
import de.symeda.sormas.app.backend.region.District;
import de.symeda.sormas.app.backend.region.Region;
import de.symeda.sormas.app.component.FieldHelper;
import de.symeda.sormas.app.component.LocationDialogBuilder;
import de.symeda.sormas.app.component.PropertyField;
import de.symeda.sormas.app.component.SpinnerField;
import de.symeda.sormas.app.component.TextField;
import de.symeda.sormas.app.databinding.PersonEditFragmentLayoutBinding;
import de.symeda.sormas.app.util.Consumer;
import de.symeda.sormas.app.util.DataUtils;
import de.symeda.sormas.app.util.FormTab;
import de.symeda.sormas.app.util.Item;
import de.symeda.sormas.app.validation.PersonValidator;

import static de.symeda.sormas.app.component.FacilityChangeDialogBuilder.NONE_HEALTH_FACILITY_DETAILS;

/**
 * Created by Stefan Szczesny on 27.07.2016.
 */
public class PersonEditForm extends FormTab {

    private PersonEditFragmentLayoutBinding binding;

    private Disease disease;
    private String diseaseDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.person_edit_fragment_layout, container, false);
        editOrCreateUserRight = (UserRight) getArguments().get(EDIT_OR_CREATE_USER_RIGHT);

        final String personUuid = getArguments().getString(Person.UUID);
        PersonDao personDao = DatabaseHelper.getPersonDao();
        Person person = personDao.queryUuid(personUuid);
        binding.setPerson(person);

        disease = (Disease) getArguments().get(Case.DISEASE);
        diseaseDetails = getArguments().getString(Case.DISEASE_DETAILS);

        // date of birth
        FieldHelper.initSpinnerField(binding.personBirthdateDD, DataUtils.toItems(DateHelper.getDaysInMonth(),true), new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateApproximateAgeField();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        FieldHelper.initSpinnerField(binding.personBirthdateMM, DataUtils.getMonthItems(), new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateApproximateAgeField();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        FieldHelper.initSpinnerField(binding.personBirthdateYYYY, DataUtils.toItems(DateHelper.getYearsToNow(),true), new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateApproximateAgeField();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.personBirthdateYYYY.setSelectionOnOpen(2000);

        // age type
        FieldHelper.initSpinnerField(binding.personApproximateAgeType, ApproximateAgeType.class);

        // gender
        FieldHelper.initSpinnerField(binding.personSex, Sex.class);

        // date of death
        binding.personDeathDate.initialize(this);
        binding.personDeathDate.addValueChangedListener(new PropertyField.ValueChangeListener() {
            @Override
            public void onChange(PropertyField field) {
                updateApproximateAgeField();
            }
        });

        binding.personBurialDate.initialize(this);

        FieldHelper.initSpinnerField(binding.personDeathPlaceType, DeathPlaceType.class);
        FieldHelper.initSpinnerField(binding.personBurialConductor, BurialConductor.class);
        FieldHelper.initSpinnerField(binding.personCauseOfDeath, CauseOfDeath.class);
        FieldHelper.initSpinnerField(binding.personCauseOfDeathDisease, Disease.class);

        // status of patient
        FieldHelper.initSpinnerField(binding.personPresentCondition, PresentCondition.class, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.personPresentCondition.getValue() == PresentCondition.DEAD ||
                        binding.personPresentCondition.getValue() == PresentCondition.BURIED) {
                    binding.personCauseOfDeath.setValue(CauseOfDeath.EPIDEMIC_DISEASE);
                }

                updateDeathAndBurialFields(disease);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                updateDeathAndBurialFields(disease);
            }
        });

        // evaluate the model and set the ui
        updateDeathAndBurialFields(disease);
        updateApproximateAgeField();

        // ================ Address ================
        LocationDialogBuilder.addLocationField(getActivity(), person.getAddress(), binding.personAddress, binding.formCpBtnAddress, new Consumer() {
            @Override
            public void accept(Object parameter) {
                if(parameter instanceof Location) {
                    binding.personAddress.setValue(parameter.toString());
                    binding.getPerson().setAddress(((Location)parameter));
                }
            }
        });

        // ================ Occupation ================

        final TextField occupationDetails = binding.personOccupationDetails;
        FieldHelper.initSpinnerField(binding.personOccupationType, OccupationType.class, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item)parent.getItemAtPosition(position);
                updateVisibilityOccupationFields(item, occupationDetails);
                updateHeadlineOccupationDetailsFields(item, occupationDetails);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                deactivateField(occupationDetails);
            }
        });

        initFacilityFields(person.getOccupationFacility());

        // ================ Additional settings ================

        binding.personApproximate1Age.setInputType(InputType.TYPE_CLASS_NUMBER);
        binding.personPhone.setInputType(InputType.TYPE_CLASS_PHONE);

        toggleCauseOfDeathFields(binding.personPresentCondition.getValue() != null &&
                binding.personPresentCondition.getValue() != PresentCondition.ALIVE);

        binding.personCauseOfDeath.addValueChangedListener(new PropertyField.ValueChangeListener() {
            @Override
            public void onChange(PropertyField field) {
                toggleCauseOfDeathFields(true);
            }
        });

        binding.personCauseOfDeathDisease.addValueChangedListener(new PropertyField.ValueChangeListener() {
            @Override
            public void onChange(PropertyField field) {
                toggleCauseOfDeathFields(true);
            }
        });

        PersonValidator.setRequiredHintsForPersonData(binding);
        binding.personPresentCondition.makeFieldSoftRequired();
        binding.personSex.makeFieldSoftRequired();
        binding.personDeathDate.makeFieldSoftRequired();
        binding.personDeathPlaceDescription.makeFieldSoftRequired();
        binding.personDeathPlaceType.makeFieldSoftRequired();
        binding.personCauseOfDeath.makeFieldSoftRequired();
        binding.personCauseOfDeathDetails.makeFieldSoftRequired();
        binding.personCauseOfDeathDisease.makeFieldSoftRequired();
        binding.personBurialDate.makeFieldSoftRequired();
        binding.personBurialPlaceDescription.makeFieldSoftRequired();
        binding.personBurialConductor.makeFieldSoftRequired();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // @TODO: Workaround, find a better solution. Remove autofocus on first field.
        getView().requestFocus();
    }

    private void updateHeadlineOccupationDetailsFields(Item item, TextField occupationDetails) {
        if(item.getValue()!=null) {
            switch ((OccupationType) item.getValue()) {
                case BUSINESSMAN_WOMAN:
                    occupationDetails.updateCaption(getResources().getString(R.string.headline_form_cp_occupation_details_business));
                    break;
                case TRANSPORTER:
                    occupationDetails.updateCaption(getResources().getString(R.string.headline_form_cp_occupation_details_transport));
                    break;
                case OTHER:
                    occupationDetails.updateCaption(getResources().getString(R.string.headline_form_cp_occupation_details_other));
                    break;
                case HEALTHCARE_WORKER:
                    occupationDetails.updateCaption(getResources().getString(R.string.headline_form_cp_occupation_details_healthcare));
                    break;
                default:
                    occupationDetails.updateCaption(getResources().getString(R.string.headline_form_cp_occupation_details));
                    break;
            }
        }
        else {
            occupationDetails.updateCaption(getResources().getString(R.string.headline_form_cp_occupation_details));
        }
    }

    private void updateVisibilityOccupationFields(Item item, View occupationDetails) {
        if(item.getValue()!=null) {
            switch ((OccupationType) item.getValue()) {
                case BUSINESSMAN_WOMAN:
                case TRANSPORTER:
                case OTHER:
                    occupationDetails.setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.person_facility_layout).setVisibility(View.GONE);
                    break;
                case HEALTHCARE_WORKER:
                    occupationDetails.setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.person_facility_layout).setVisibility(View.VISIBLE);
                    break;
                default:
                    occupationDetails.setVisibility(View.GONE);
                    getActivity().findViewById(R.id.person_facility_layout).setVisibility(View.GONE);
                    break;
            }
        }
        else {
            occupationDetails.setVisibility(View.GONE);
            getActivity().findViewById(R.id.person_facility_layout).setVisibility(View.GONE);
        }
    }

    private void initFacilityFields(Facility facility) {
        final List emptyList = new ArrayList<>();
        final List districtsByRegion = DataUtils.toItems(binding.getPerson().getOccupationRegion() != null ? DatabaseHelper.getDistrictDao().getByRegion(binding.getPerson().getOccupationRegion()) : DataUtils.toItems(emptyList), true);
        final List communitiesByDistrict = DataUtils.toItems(binding.getPerson().getOccupationDistrict() != null ? DatabaseHelper.getCommunityDao().getByDistrict(binding.getPerson().getOccupationDistrict()) : DataUtils.toItems(emptyList), true);
        final List facilities = DataUtils.toItems(binding.getPerson().getOccupationCommunity() != null ? DatabaseHelper.getFacilityDao().getHealthFacilitiesByCommunity(binding.getPerson().getOccupationCommunity(), true) :
                binding.getPerson().getOccupationDistrict() != null ? DatabaseHelper.getFacilityDao().getHealthFacilitiesByDistrict(binding.getPerson().getOccupationDistrict(), true) : DataUtils.toItems(emptyList), true);

        FieldHelper.initRegionSpinnerField(binding.personOccupationRegion, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object selectedValue = binding.personOccupationRegion.getValue();
                if(binding.personOccupationDistrict != null) {
                    List<District> districtList = emptyList;
                    if(selectedValue != null) {
                        districtList = DatabaseHelper.getDistrictDao().getByRegion((Region)selectedValue);
                    } else {
                        binding.personOccupationDistrict.setValue(null);
                    }
                    binding.personOccupationDistrict.setAdapterAndValue(binding.personOccupationDistrict.getValue(), DataUtils.toItems(districtList));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        FieldHelper.initSpinnerField(binding.personOccupationDistrict, districtsByRegion, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object selectedValue = binding.personOccupationDistrict.getValue();
                if(binding.personOccupationCommunity != null) {
                    List<Community> communityList = emptyList;
                    List<Facility> facilityList = emptyList;
                    if(selectedValue != null) {
                        communityList = DatabaseHelper.getCommunityDao().getByDistrict((District)selectedValue);
                        facilityList = DatabaseHelper.getFacilityDao().getHealthFacilitiesByDistrict((District) selectedValue, true);
                    } else {
                        binding.personOccupationCommunity.setValue(null);
                    }
                    binding.personOccupationCommunity.setAdapterAndValue(binding.personOccupationCommunity.getValue(), DataUtils.toItems(communityList));
                    binding.personOccupationFacility.setAdapterAndValue(binding.personOccupationFacility.getValue(), DataUtils.toItems(facilityList));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        FieldHelper.initSpinnerField(binding.personOccupationCommunity, communitiesByDistrict, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerField spinnerField = binding.personOccupationFacility;
                Object selectedValue = binding.personOccupationCommunity.getValue();
                if (spinnerField != null) {
                    List<Facility> facilityList = emptyList;
                    if (selectedValue != null) {
                        facilityList = DatabaseHelper.getFacilityDao().getHealthFacilitiesByCommunity((Community) selectedValue, true);
                    } else if (binding.personOccupationDistrict.getValue() != null) {
                        facilityList = DatabaseHelper.getFacilityDao().getHealthFacilitiesByDistrict((District) binding.personOccupationDistrict.getValue(), true);
                    } else {
                        spinnerField.setValue(null);
                    }
                    spinnerField.setAdapterAndValue(binding.personOccupationFacility.getValue(), DataUtils.toItems(facilityList));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        FieldHelper.initSpinnerField(binding.personOccupationFacility, facilities);

        binding.personOccupationFacility.addValueChangedListener(new PropertyField.ValueChangeListener() {
            @Override
            public void onChange(PropertyField field) {
                TextField facilityDetailsField = binding.personOccupationFacilityDetails;

                Facility selectedFacility = (Facility) field.getValue();
                if (selectedFacility != null) {
                    boolean otherHealthFacility = selectedFacility.getUuid().equals(FacilityDto.OTHER_FACILITY_UUID);
                    boolean noneHealthFacility = selectedFacility.getUuid().equals(FacilityDto.NONE_FACILITY_UUID);
                    if (otherHealthFacility) {
                        facilityDetailsField.setVisibility(View.VISIBLE);
                        facilityDetailsField.setCaption(I18nProperties.getPrefixFieldCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.HEALTH_FACILITY_DETAILS));
                    } else if (noneHealthFacility) {
                        facilityDetailsField.setVisibility(View.VISIBLE);
                        facilityDetailsField.setCaption(I18nProperties.getPrefixFieldCaption(CaseDataDto.I18N_PREFIX, NONE_HEALTH_FACILITY_DETAILS));
                    } else {
                        facilityDetailsField.setVisibility(View.GONE);
                    }
                } else {
                    facilityDetailsField.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updateDeathAndBurialFields(Disease disease) {
        List<PropertyField<?>> deathAndBurialFields = Arrays.asList(binding.personDeathPlaceType, binding.personDeathPlaceDescription,
                binding.personBurialDate, binding.personBurialConductor, binding.personBurialPlaceDescription);

        PresentCondition condition = (PresentCondition)binding.personPresentCondition.getValue();
        setFieldVisible(binding.personDeathDate, condition != null
                && (PresentCondition.DEAD.equals(condition) || PresentCondition.BURIED.equals(condition)));
        setFieldVisibleOrGone(binding.personDeathPlaceType, condition != null
                && (PresentCondition.DEAD.equals(condition) || PresentCondition.BURIED.equals(condition)));
        setFieldVisibleOrGone(binding.personDeathPlaceDescription, condition != null
                && (PresentCondition.DEAD.equals(condition) || PresentCondition.BURIED.equals(condition)));
        setFieldVisibleOrGone(binding.personBurialDate, condition != null && PresentCondition.BURIED.equals(condition));
        setFieldVisibleOrGone(binding.personBurialConductor, condition != null && PresentCondition.BURIED.equals(condition));
        setFieldVisibleOrGone(binding.personBurialPlaceDescription, condition != null && PresentCondition.BURIED.equals(condition));

        toggleCauseOfDeathFields(condition != null && condition != PresentCondition.ALIVE);

        // Make sure that death and burial fields are only shown for EVD
        for (PropertyField<?> field : deathAndBurialFields) {
            String propertyId = field.getPropertyId();
            boolean definedOrMissing = Diseases.DiseasesConfiguration.isDefinedOrMissing(PersonDto.class, propertyId, disease);
            if (!definedOrMissing) {
                field.setVisibility(View.GONE);
            }
        }

        fillDeathAndBurialFields();
        binding.personDeathDate.clearFocus();
    }

    private void fillDeathAndBurialFields() {
        if (binding.personDeathPlaceType.getVisibility() == View.VISIBLE && binding.personDeathPlaceType.getValue() == null) {
            binding.personDeathPlaceType.setValue(DeathPlaceType.OTHER);
            if (binding.personDeathPlaceDescription.getVisibility() == View.VISIBLE && (binding.personDeathPlaceDescription.getValue() == null || binding.personDeathPlaceDescription.getValue().isEmpty())) {
                binding.personDeathPlaceDescription.setValue(binding.getPerson().getAddress().toString());
            }
        }

        if (binding.personBurialPlaceDescription.getVisibility() == View.VISIBLE && (binding.personBurialPlaceDescription.getValue() == null || binding.personBurialPlaceDescription.getValue().isEmpty())) {
            binding.personBurialPlaceDescription.setValue(binding.getPerson().getAddress().toString());
        }
    }

    private void updateApproximateAgeField() {
        Integer birthyear = (Integer)binding.personBirthdateYYYY.getValue();
        TextField approximateAgeTextField = binding.personApproximate1Age;
        SpinnerField approximateAgeTypeField = binding.personApproximateAgeType;

        if(birthyear!=null) {
            deactivateField(approximateAgeTextField);
            deactivateField(approximateAgeTypeField);

            Integer birthday = (Integer)binding.personBirthdateDD.getValue();
            Integer birthmonth = (Integer)binding.personBirthdateMM.getValue();

            Calendar birthDate = new GregorianCalendar();
            birthDate.set(birthyear, birthmonth!=null?birthmonth-1:0, birthday!=null?birthday:1);

            Date to = new Date();
            if(binding.personDeathDate.getValue() != null) {
                to = binding.personDeathDate.getValue();
            }
            DataHelper.Pair<Integer, ApproximateAgeType> approximateAge = ApproximateAgeHelper.getApproximateAge(birthDate.getTime(),to);
            ApproximateAgeType ageType = approximateAge.getElement1();
            approximateAgeTextField.setValue(String.valueOf(approximateAge.getElement0()));
            for (int i=0; i<approximateAgeTypeField.getCount(); i++) {
                Item item = (Item)approximateAgeTypeField.getItemAtPosition(i);
                if (item != null && item.getValue() != null && item.getValue().equals(ageType)) {
                    approximateAgeTypeField.setValue(i);
                    break;
                }
            }
        } else {
            approximateAgeTextField.setEnabled(true, editOrCreateUserRight);
            approximateAgeTypeField.setEnabled(true, editOrCreateUserRight);
        }
    }

    private void toggleCauseOfDeathFields(boolean causeOfDeathVisible) {
        if (!causeOfDeathVisible) {
            binding.personCauseOfDeath.setVisibility(View.GONE);
            binding.personCauseOfDeathDetails.setVisibility(View.GONE);
            binding.personCauseOfDeathDisease.setVisibility(View.GONE);
        } else {
            binding.personCauseOfDeath.setVisibility(View.VISIBLE);

            if (binding.personCauseOfDeath.getValue() == null) {
                binding.personCauseOfDeathDisease.setValue(null);
                binding.personCauseOfDeathDetails.setValue(null);
                binding.getPerson().setCauseOfDeathDisease(null);
                binding.getPerson().setCauseOfDeathDetails(null);
                binding.personCauseOfDeathDetails.setVisibility(View.GONE);
                binding.personCauseOfDeathDisease.setVisibility(View.GONE);
            } else if (binding.personCauseOfDeath.getValue() == CauseOfDeath.EPIDEMIC_DISEASE) {
                binding.personCauseOfDeathDisease.setVisibility(View.VISIBLE);
                if (binding.personCauseOfDeathDisease.getValue() == Disease.OTHER) {
                    binding.personCauseOfDeathDetails.setVisibility(View.VISIBLE);
                } else {
                    binding.personCauseOfDeathDetails.setVisibility(View.GONE);
                }
                if (binding.personCauseOfDeathDisease.getValue() == null) {
                    binding.personCauseOfDeathDisease.setValue(disease);
                }
                if (disease == Disease.OTHER) {
                    binding.personCauseOfDeathDetails.setValue(diseaseDetails);
                }
            } else {
                binding.personCauseOfDeathDisease.setValue(null);
                binding.getPerson().setCauseOfDeathDisease(null);
                binding.personCauseOfDeathDisease.setVisibility(View.GONE);
                binding.personCauseOfDeathDetails.setVisibility(View.VISIBLE);
            }
        }
    }

    public PersonEditFragmentLayoutBinding getBinding() {
        return binding;
    }

    @Override
    public AbstractDomainObject getData() {

        return binding == null ? null : binding.getPerson();
    }

}