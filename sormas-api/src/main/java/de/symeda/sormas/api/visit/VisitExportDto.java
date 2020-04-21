package de.symeda.sormas.api.visit;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.DiseaseHelper;
import de.symeda.sormas.api.importexport.ExportGroup;
import de.symeda.sormas.api.importexport.ExportGroupType;
import de.symeda.sormas.api.importexport.ExportProperty;
import de.symeda.sormas.api.importexport.ExportTarget;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.symptoms.SymptomsDto;
import de.symeda.sormas.api.utils.Order;

import java.io.Serializable;
import java.util.Date;

public class VisitExportDto implements Serializable {

    public static final String I18N_PREFIX = "VisitExport";

    private Long id;
    private String uuid;
    private Long personId;
    private Long symptomsId;
    private String firstName;
    private String lastName;
    private String diseaseFormatted;
    private Date visitDateTime;
    private Long visitUserId;
    private VisitStatus visitStatus;
    private String visitRemarks;
    private SymptomsDto symptoms;

    private Double reportLat;
    private Double reportLon;

    public VisitExportDto(Long id, String uuid, Long personId, String firstName, String lastName, Long symptomsId, Long visitUserId, Disease disease, Date visitDateTime,
                          VisitStatus visitStatus, String visitRemarks, Double reportLat, Double reportLon) {
        this.id = id;
        this.uuid = uuid;
        this.personId = personId;
        this.firstName  = firstName;
        this.lastName = lastName;
        this.symptomsId = symptomsId;
        this.diseaseFormatted = DiseaseHelper.toString(disease, null);
        this.visitDateTime = visitDateTime;
        this.visitUserId = visitUserId;
        this.visitStatus = visitStatus;
        this.visitRemarks = visitRemarks;
        this.reportLat = reportLat;
        this.reportLon = reportLon;
    }

    public Long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public Long getSymptomsId() {
        return symptomsId;
    }

    public Long getPersonId() {
        return personId;
    }

    @Order(2)
    @ExportTarget(visitExportTypes = {VisitExportType.CONTACT_VISITS})
    @ExportProperty(PersonDto.FIRST_NAME)
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getFirstName() {
        return firstName;
    }

    @Order(3)
    @ExportTarget(visitExportTypes = {VisitExportType.CONTACT_VISITS})
    @ExportProperty(PersonDto.LAST_NAME)
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getLastName() {
        return lastName;
    }

    @Order(4)
    @ExportTarget(visitExportTypes = {VisitExportType.CONTACT_VISITS})
    @ExportProperty(VisitDto.DISEASE)
    @ExportGroup(ExportGroupType.CORE)
    public String getDiseaseFormatted() {
        return diseaseFormatted;
    }

    @Order(5)
    @ExportTarget(visitExportTypes = {VisitExportType.CONTACT_VISITS})
    @ExportProperty(VisitDto.VISIT_DATE_TIME)
    @ExportGroup(ExportGroupType.CORE)
    public Date getVisitDateTime() {
        return visitDateTime;
    }

    public Long getVisitUserId() {
        return visitUserId;
    }

    @Order(6)
    @ExportTarget(visitExportTypes = {VisitExportType.CONTACT_VISITS})
    @ExportProperty(VisitDto.VISIT_STATUS)
    @ExportGroup(ExportGroupType.CORE)
    public VisitStatus getVisitStatus() {
        return visitStatus;
    }

    @Order(7)
    @ExportTarget(visitExportTypes = {VisitExportType.CONTACT_VISITS})
    @ExportProperty(VisitDto.VISIT_REMARKS)
    @ExportGroup(ExportGroupType.CORE)
    public String getVisitRemarks() {
        return visitRemarks;
    }

    @Order(8)
    @ExportTarget(visitExportTypes = {VisitExportType.CONTACT_VISITS})
    @ExportProperty(VisitDto.REPORT_LAT)
    @ExportGroup(ExportGroupType.CORE)
    public Double getReportLat() {
        return reportLat;
    }

    @Order(9)
    @ExportTarget(visitExportTypes = {VisitExportType.CONTACT_VISITS})
    @ExportProperty(VisitDto.REPORT_LON)
    @ExportGroup(ExportGroupType.CORE)
    public Double getReportLon() {
        return reportLon;
    }

    @Order(10)
    @ExportTarget(visitExportTypes = {VisitExportType.CONTACT_VISITS})
    @ExportProperty(VisitDto.SYMPTOMS)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public SymptomsDto getSymptoms() {
        return symptoms;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setSymptomsId(Long symptomsId) {
        this.symptomsId = symptomsId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public void setDiseaseFormatted(String diseaseFormatted) {
        this.diseaseFormatted = diseaseFormatted;
    }

    public void setVisitDateTime(Date visitDateTime) {
        this.visitDateTime = visitDateTime;
    }

    public void setVisitUserId(Long visitUserId) {
        this.visitUserId = visitUserId;
    }

    public void setVisitStatus(VisitStatus visitStatus) {
        this.visitStatus = visitStatus;
    }

    public void setVisitRemarks(String visitRemarks) {
        this.visitRemarks = visitRemarks;
    }

    public void setSymptoms(SymptomsDto symptoms) {
        this.symptoms = symptoms;
    }

    public void setReportLat(Double reportLat) {
        this.reportLat = reportLat;
    }

    public void setReportLon(Double reportLon) {
        this.reportLon = reportLon;
    }
}