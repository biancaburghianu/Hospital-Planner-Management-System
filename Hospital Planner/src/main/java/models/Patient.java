package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "patients")
@EntityListeners(AuditingEntityListener.class)
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @Column(name = "email", nullable = false)
    private String patientEmail;

    @Column(name = "phone_number", nullable = false)
    private String patientPhoneNumber;

    @Column(name = "password", nullable = false)
    private String patientPassword;

    @Column(name = "birth_date", nullable = false)
    private String patientBirthDate;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientPhoneNumber() {
        return patientPhoneNumber;
    }

    public void setPatientPhoneNumber(String patientPhoneNumber) {
        this.patientPhoneNumber = patientPhoneNumber;
    }

    public String getPatientPassword() {
        return patientPassword;
    }

    public void setPatientPassword(String patientPassword) {
        this.patientPassword = patientPassword;
    }

    public String getPatientBirthDate() {
        return patientBirthDate;
    }

    public void setPatientBirthDate(String patientBirthDate) {
        this.patientBirthDate = patientBirthDate;
    }

    public Patient(long id, String patientName, String patientEmail, String patientPhoneNumber, String patientPassword, String patientBirthDate) {
        this.id = id;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPhoneNumber = patientPhoneNumber;
        this.patientPassword = patientPassword;
        this.patientBirthDate = patientBirthDate;
    }
}
