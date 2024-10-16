package ru.mirea.pkmn.shpagintp;

import ru.mirea.pkmn.*;

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
            card_from = new Card();
            CardImport cardimport = new CardImport();
            card_from = cardimport.Fill(reportData[4]);
        }

        List<AttackSkill> skills = new ArrayList<>();
        String[] first = reportData[5].split(",");
        String[][] second = new String[first.length][];
        for (int i = 0; i < first.length; i++) {
            second[i] = first[i].split("/");
        }
        for (int i = 0; i < first.length; i=i+3) {
            AttackSkill attackSkill = new AttackSkill(second[i][0], "-", second[i][1], Integer.parseInt(second[i][2]));
            skills.add(attackSkill);
        }

        String[] student_ = reportData[11].split("/");
        Student student = new Student(student_[1], student_[0], student_[2], student_[3]);

        EnergyType weakness;
        try {
            weakness = EnergyType.valueOf(reportData[6].toUpperCase());
        } catch (IllegalArgumentException e) {
            weakness=null;
        }

        EnergyType resistance;
        try {
            resistance = EnergyType.valueOf(reportData[6].toUpperCase());
        } catch (IllegalArgumentException e) {
            resistance=null;
        }

        Card card = new Card(PokemonStage.valueOf(reportData[0].toUpperCase()), reportData[1], Integer.parseInt(reportData[2]),
                EnergyType.valueOf(reportData[3].toUpperCase()), card_from, skills, weakness,
                resistance, reportData[8], reportData[9], reportData[10].charAt(0), student);
        return card;
    }
        public static Card SImport(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(filename);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return (Card) objectInputStream.readObject();
    }
}
