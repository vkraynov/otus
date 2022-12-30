package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.dto.AddressDto;
import ru.otus.crm.dto.ClientDto;
import ru.otus.crm.dto.PhoneDto;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ClientsApiServlet extends HttpServlet {

    private final DBServiceClient dBServiceClient;
    private final Gson gson;

    public ClientsApiServlet(DBServiceClient dBServiceClient, Gson gson) {
        this.dBServiceClient = dBServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder jb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String stringClient = jb.toString();
        Client clientToSave = new Gson().fromJson(stringClient, Client.class);

        Client savedClient = dBServiceClient.saveClient(clientToSave);
        ClientDto clientDto = mapEntityToDto(savedClient);

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        String res = gson.toJson(clientDto);
        out.print(res);
    }

    private ClientDto mapEntityToDto(Client savedClient) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(savedClient.getId());
        clientDto.setName(savedClient.getName());
        clientDto.setAddress(new AddressDto(savedClient.getAddress().getId(), savedClient.getAddress().getStreet()));
        List<Phone> phones = savedClient.getPhones();
        List<PhoneDto> phoneDtos = new ArrayList<>();
        for (Phone phone : phones) {
            phoneDtos.add(new PhoneDto(phone.getId(), phone.getNumber()));
        }
        clientDto.setPhones(phoneDtos);

        return clientDto;
    }

}
