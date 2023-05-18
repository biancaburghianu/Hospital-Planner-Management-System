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
@Table(name = "doctors")
@EntityListeners(AuditingEntityListener.class)
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
