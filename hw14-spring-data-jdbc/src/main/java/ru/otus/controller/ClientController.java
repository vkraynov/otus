package ru.otus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.service.DBServiceClient;

/**
 * ClientController.
 *
 * @author Vadim_Kraynov
 * @since 05.02.2023
 */
@Controller
public class ClientController {
    @Autowired
    private DBServiceClient dBServiceClient;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("clients", dBServiceClient.findAll());
        return "index";
    }

    @GetMapping("/add")
    public String addClient(Model model) {
        model.addAttribute("client", new Client(null, null));
        return "create";
    }

    @PostMapping("/save")
    public String updateClient(@RequestParam(required = false) Long id,
                               @RequestParam String name,
                               @RequestParam String address) {
        dBServiceClient.saveClient(new Client(id, name, new Address(address)));
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String updateForm(@PathVariable(value = "id") long id, Model model) {
        model.addAttribute("client", dBServiceClient.getClient(id)
                .orElseThrow(() -> new RuntimeException("Client not found")));
        return "update";
    }

    @GetMapping("/deleteClient/{id}")
    public String deleteThroughId(@PathVariable(value = "id") long id) {
        dBServiceClient.delete(id);
        return "redirect:/";
    }
}
