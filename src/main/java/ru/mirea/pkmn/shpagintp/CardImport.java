package ru.mirea.pkmn.shpagintp;

import com.fasterxml.jackson.databind.JsonNode;
import ru.mirea.pkmn.*;
import ru.mirea.pkmn.shpagintp.web.http.PkmnHttpClient;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class CardImport {
    private String[] reportData;

    public CardImport() {}

    private void Import(String filename) throws IOException {
        FileInputStream fileinput = new FileInputStream(filename);
        BufferedInputStream bufferedinput = new BufferedInputStream(fileinput);
        byte[] data = bufferedinput.readAllBytes();
        reportData = new String(data).split("\r\n");
        fileinput.close();
        bufferedinput.close();
    }

    public Card Fill(String filename) throws IOException {
        Import(filename);
        Card card_from = null;
        if (!reportData[4].equals("-")) {
            CardImport cardimport = new CardImport();
            card_from = cardimport.Fill(reportData[4]);
        }

        List<AttackSkill> skills = new ArrayList<>();
        PkmnHttpClient pkmnHttpClient = new PkmnHttpClient();
        JsonNode jsonResponse = pkmnHttpClient.getPokemonCard(reportData[1], reportData[12]);

        // Извлекаем информацию об атаках
        JsonNode attacksNode = jsonResponse.path("data").get(0).path("attacks");

        for (JsonNode attackNode : attacksNode) {
            String name = attackNode.path("name").asText();

            // Получаем конвертированную стоимость
            int convertedEnergyCost = attackNode.path("convertedEnergyCost").asInt();

            // Получаем текст описания
            String description = attackNode.path("text").asText();

            // Получаем урон, преобразуем в целое число
            String damageString = attackNode.path("damage").asText();
            int damage = damageString.contains("×") ? Integer.parseInt(damageString.replace("×", "").trim()) : Integer.parseInt(damageString);

            AttackSkill attackSkill = new AttackSkill(name, description, String.valueOf(convertedEnergyCost), damage);
            skills.add(attackSkill);
        }

        String[] student_ = reportData[11].split("/");
        Student student = new Student(student_[1], student_[0], student_[2], student_[3]);

        EnergyType weakness = null;
        EnergyType resistance = null;

        try {
            weakness = EnergyType.valueOf(reportData[6].toUpperCase());
        } catch (IllegalArgumentException e) {
            weakness = null;
        }

        try {
            resistance = EnergyType.valueOf(reportData[7].toUpperCase());
        } catch (IllegalArgumentException e) {
            resistance = null;
        }

        Card card = new Card(
                PokemonStage.valueOf(reportData[0].toUpperCase()),
                reportData[1],
                Integer.parseInt(reportData[2]),
                EnergyType.valueOf(reportData[3].toUpperCase()),
                card_from,
                skills,
                weakness,
                resistance,
                reportData[8],
                reportData[9],
                reportData[10].charAt(0),
                student,
                reportData[12]
        );

        return card;
    }
    public static Card SImport(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(filename);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return (Card) objectInputStream.readObject();
    }
}
