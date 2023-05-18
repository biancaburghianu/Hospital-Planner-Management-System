package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "doctors")
@EntityListeners(AuditingEntityListener.class)
public class Doctor {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "doctor_name", nullable = false)
    private String doctorName;

    @Column(name = "email", nullable = false)
    private String doctorEmail;

    @Column(name = "phone_number", nullable = false)
    private String doctorPhoneNumber;

    @Column(name = "password", nullable = false)
    private String doctorPassword;

    @Column(name = "office", nullable = false)
    private String doctorOffice;

    @Column(name = "specialisation", nullable = false)
    private String doctorSpecialisation;

    public Doctor() {
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getDoctorPhoneNumber() {
        return doctorPhoneNumber;
    }

    public void setDoctorPhoneNumber(String doctorPhoneNumber) {
        this.doctorPhoneNumber = doctorPhoneNumber;
    }

    public String getDoctorPassword() {
        return doctorPassword;
    }

    public void setDoctorPassword(String doctorPassword) {
        this.doctorPassword = doctorPassword;
    }

    public String getDoctorOffice() {
        return doctorOffice;
    }

    public void setDoctorOffice(String doctorOffice) {
        this.doctorOffice = doctorOffice;
    }

    public String getDoctorSpecialisation() {
        return doctorSpecialisation;
    }

    public void setDoctorSpecialisation(String doctorSpecialisation) {
        this.doctorSpecialisation = doctorSpecialisation;
    }

    public Doctor(Integer id, String doctorName, String doctorEmail, String doctorPhoneNumber, String doctorPassword, String doctorOffice, String doctorSpecialisation) {
        this.id = id;
        this.doctorName = doctorName;
        this.doctorEmail = doctorEmail;
        this.doctorPhoneNumber = doctorPhoneNumber;
        this.doctorPassword = doctorPassword;
        this.doctorOffice = doctorOffice;
        this.doctorSpecialisation = doctorSpecialisation;
    }
}
