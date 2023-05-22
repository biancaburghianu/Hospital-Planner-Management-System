package com.example.controllers;

import com.example.models.Appointment;
import com.example.models.Doctor;
import com.example.models.Patient;
import com.example.repositories.AppointmentRepository;
import com.example.repositories.DoctorRepository;
import com.example.repositories.PatientRepository;
import com.example.utils.AppointmentRequest;
import com.example.utils.HospitalProgram;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AppointmentController {

    @Autowired
    private final AppointmentRepository appointmentRepository;
    @Autowired
    private final DoctorRepository doctorRepository;
    @Autowired
    private final PatientRepository patientRepository;

    @Autowired
    public AppointmentController(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @PostMapping("/appointments")
    public ResponseEntity<String> createAppointment(@RequestBody AppointmentRequest request) throws IOException {
        Integer doctorId = request.getDoctorId();
        Integer patientId = request.getPatientId();

        //verificam daca doctorul exista in baza de date
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Doctorul nu exista in baza de date");
        }

        //verificam daca pacientul exista in baza de date
        Patient patient = patientRepository.findById(patientId).orElse(null);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Pacientul nu exista in baza de date");
        }
        // verificam daca ora este valida
        HospitalProgram hospitalProgram = new HospitalProgram();

        if (!!HospitalProgram.isValid(request.getTime())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ora specificata nu se afla in programul spitalului nostru.");
        }
        // creeaza obiectul Appointment
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setTime(request.getTime());
        appointment.setDescription(request.getDescription());

        //verificam daca doctorul nu are deja o programare la ora specificata
        List<String> doctorAppointments = appointmentRepository.findAppointmentTimesByDoctorId(doctorId);
        if (doctorAppointments.contains(appointment.getTime())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Doctorul are deja o programare la ora specificata de tine");
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        if (savedAppointment != null) {

            Email from = new Email("hospitalplannerjava@gmail.com");
            String subject = "Programare in clinica HospitalPlannerJava";
            Email to = new Email(patient.getPatientEmail());
            Content content = new Content("text/plain", "Informatii legate de programarea ta: " +
                    "Programarea incepe la ora: "+ appointment.getTime() + " . Sediul nostru este in Iasi, pe strada HospitalPlannerJava. "+
                    "Doctorul la care ai programare este: " + doctorRepository.findDoctorName(request.getDoctorId()) + ". Iti multumim!");
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid("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");
            Request requestMail = new Request();
            try {
                requestMail.setMethod(Method.POST);
                requestMail.setEndpoint("mail/send");
                requestMail.setBody(mail.build());
                Response response = sg.api(requestMail);
                return ResponseEntity.ok("Programarea a fost creata cu succes! Te asteptam in clinica noastra la ora " + appointment.getTime());
            } catch (IOException ex) {
                throw ex;
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Programarea ta nu a putut fi salvata");
        }
    }

    @DeleteMapping("/appointments/{id}")
    public String deleteAppointment(@PathVariable("id") Integer id)
    {
        Optional<Appointment> appointment=appointmentRepository.findById(id);
        if(appointment.isPresent())
        {
            appointmentRepository.deleteById(id);

            return "S-a sters";
        }
        return "Nu s-a sters";
    }
    @GetMapping("/appointments/{id}")
    public String findAppointment(@PathVariable("id") Integer id)
    {
        Optional<Appointment> appointment=appointmentRepository.findById(id);
        if (appointment.isPresent()) {
            Appointment appointmentObj = appointment.get();
            Doctor doctorObj = appointmentObj.getDoctor();
            String doctorName = doctorObj.getDoctorName();

            return "Appointment ID: " + appointmentObj.getId() +
                    "\nTime: " + appointmentObj.getTime() +
                    "\nDescription: " + appointmentObj.getDescription() +
                    "\nDoctor Name: " + doctorName;
        }
        return "Nu exista";
    }
}

