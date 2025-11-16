package ru.eonSpring.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.eonSpring.crm.model.Address;
import ru.eonSpring.crm.model.Client;
import ru.eonSpring.crm.model.Phone;
import ru.eonSpring.crm.repository.PhoneRepository;
import ru.eonSpring.crm.service.DBServiceClient;

@Controller
public class ClientController {

    private final DBServiceClient dbServiceClient;
    private final PhoneRepository phoneRepository;

    public ClientController(DBServiceClient dbServiceClient, PhoneRepository phoneRepository) {
        this.dbServiceClient = dbServiceClient;
        this.phoneRepository = phoneRepository;
    }

    @GetMapping("/")
    public String clientsPage(Model model) {
        List<Client> clients = dbServiceClient.findAll();
        model.addAttribute("clients", clients);
        model.addAttribute("newClient", new ClientForm());
        return "clients";
    }

    @PostMapping("/clients")
    public String createClient(@ModelAttribute ClientForm clientForm) {
        Address address = null;
        if (clientForm.getStreet() != null && !clientForm.getStreet().trim().isEmpty()) {
            address = new Address(null, clientForm.getStreet().trim());
        }
        Client savedClient = new Client(null, clientForm.name, address, new ArrayList<>(), true);
        dbServiceClient.saveClient(savedClient);

        List<Phone> phones = new ArrayList<>();
        if (clientForm.getPhoneNumbers() != null
                && !clientForm.getPhoneNumbers().isEmpty()) {

            for (String phoneNumber : clientForm.getPhoneNumbers()) {
                if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                    Phone phone = new Phone(null, phoneNumber.trim(), savedClient.getId());
                    phones.add(phone);
                }
            }
        }
        phoneRepository.saveAll(phones);

        return "redirect:/";
    }

    @Setter
    @Getter
    public static class ClientForm {
        private String name;
        private String street;
        private List<String> phoneNumbers = new ArrayList<>();
    }
}
