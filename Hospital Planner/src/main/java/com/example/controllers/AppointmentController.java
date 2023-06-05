package com.example.controllers;

import com.example.models.Appointment;
import com.example.models.Doctor;
import com.example.models.Patient;
import com.example.models.Preference;
import com.example.repositories.AppointmentRepository;
import com.example.repositories.DoctorRepository;
import com.example.repositories.PatientRepository;
import com.example.service.JwtService;
import com.example.utils.AppointmentRequest;
import com.example.utils.HospitalProgram;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log
@RestController
@RequestMapping("/hospitalplanner")
public class AppointmentController {

    @Autowired
    private final AppointmentRepository appointmentRepository;
    @Autowired
    private final DoctorRepository doctorRepository;
    @Autowired
    private final PatientRepository patientRepository;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public AppointmentController(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @GetMapping("/appointments")
    public Set<Appointment> getAllAppoitments(){
        Patient currentUser = getPatientFromToken();
        if (currentUser != null) {
            return currentUser.getAppointments(); // Presupunând că există o metodă getPreferences() pe obiectul Patient pentru a obține lista de preferințe.
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @PostMapping("/appointments")
    public ResponseEntity<String> createAppointment(@RequestBody AppointmentRequest request) throws IOException {
        Integer doctorId = request.getDoctorId();

        //verificam daca doctorul exista in baza de date
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Doctorul nu exista in baza de date");
        }

        // verificam daca ora este valida
        HospitalProgram hospitalProgram = new HospitalProgram();

        if (!!HospitalProgram.isValid(request.getTime())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ora specificata nu se afla in programul spitalului nostru.");
        }

        Patient patient = getPatientFromToken();
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
            assert patient != null;
            Email to = new Email(patient.getPatientEmail());
            Content content = new Content("text/plain", "Informatii legate de programarea ta: " +
                    "Programarea incepe la ora: " + appointment.getTime() + " . Sediul nostru este in Iasi, pe strada HospitalPlannerJava. " +
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
    public ResponseEntity<String> deleteAppointment(@PathVariable("id") Integer id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isPresent()) {
            Appointment appointmentEntity = appointment.get();

            // Assuming there is a user ID associated with appointments
            Integer appointmentUserId = appointmentEntity.getPatient().getId();
            Integer appointmentDoctorId=appointmentEntity.getDoctor().getId();
            Patient currentUser = getPatientFromToken();
            Doctor currentDoctor = getDoctorFromToken();

            // Compare the appointment's user ID with the current user's ID
            if ((currentUser!=null && appointmentUserId.equals(currentUser.getId())) || (currentDoctor!=null && appointmentDoctorId.equals(currentDoctor.getId()))) {
                appointmentRepository.deleteById(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("Programarea ta a fost stearsa");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Programarea ta nu a fost gasita in lista dumneavoastra de programari");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Nu exista nicio astfel de programare");
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<String> findAppointment(@PathVariable Integer id) {
        //log.info("appointment");
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        //log.info("id");
        if (appointment.isPresent()) {
            Appointment appointmentObj = appointment.get();
            Integer appointmentUserId = appointmentObj.getPatient().getId();
            Integer appointmentDoctorId=appointmentObj.getDoctor().getId();
            Patient currentUser = getPatientFromToken();
            //log.info("got patient");
            Doctor currentDoctor = getDoctorFromToken();
            //log.info("got doctor");

            // Compare the appointment's user ID with the current user's ID
            if ((currentUser!=null && appointmentUserId.equals(currentUser.getId())) || (currentDoctor!=null && appointmentDoctorId.equals(currentDoctor.getId()))) {
                Doctor doctorObj = appointmentObj.getDoctor();
                String doctorName = doctorObj.getDoctorName();

                return ResponseEntity.ok("Appointment ID: " + appointmentObj.getId() +
                        "\nTime: " + appointmentObj.getTime() +
                        "\nDescription: " + appointmentObj.getDescription() +
                        "\nDoctor Name: " + doctorName);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Aceasta programare nu se regaseste in lista dumneavoastra de programari");
            }

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Programarea nu exista in baza noastra de date");
    }

    private Patient getPatientFromToken() {
        String token = request.getHeader("Authorization");
        String username;
        String jwtToken;
        Optional<Patient> currentUser = Optional.empty();
        if (token != null && token.startsWith("Bearer")) {
            jwtToken = token.substring(7);
            try {
                username = jwtService.extractUsername(jwtToken);
                Optional<Doctor> currentDoctor = doctorRepository.findByEmail(username);
                if (currentDoctor.isPresent()) {
                    // Return null if a patient is authenticated
                    return null;
                } else {
                    // Retrieve the doctor based on username
                    currentUser = patientRepository.findByEmail(username);
                }
            } catch (IllegalArgumentException e) {
                log.warning("Unable to get token");
            }
        } else {
            log.info("Token does not begin with Bearer String");
        }
        if (currentUser.isPresent())
            return currentUser.get();
        else throw new UsernameNotFoundException("User not found");
    }

    private Doctor getDoctorFromToken() {
        String token = request.getHeader("Authorization");
        String username;
        String jwtToken;
        Optional<Doctor> currentUser = Optional.empty();
        if (token != null && token.startsWith("Bearer")) {
            jwtToken = token.substring(7);
            try {
                username = jwtService.extractUsername(jwtToken);
                Optional<Patient> currentPatient = patientRepository.findByEmail(username);
                if (currentPatient.isPresent()) {
                    // Return null if a patient is authenticated
                    return null;
                } else {
                    // Retrieve the doctor based on username
                    currentUser = doctorRepository.findByEmail(username);
                }
            } catch (IllegalArgumentException e) {
                log.warning("Unable to get token");
            }
        } else {
            log.info("Token does not begin with Bearer String");
        }
        if (currentUser.isPresent())
        {
            return currentUser.get();
        }

        else throw new UsernameNotFoundException("User not found");
    }
}

