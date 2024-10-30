package ru.mirea.pkmn.shpagintp;


import com.fasterxml.jackson.databind.JsonNode;
import ru.mirea.pkmn.Card;
import ru.mirea.pkmn.shpagintp.web.http.PkmnHttpClient;

import java.io.IOException;
import java.util.stream.Collectors;

public class PokemonApplication {
    static String filename = "C:\\Users\\Пользователь\\IdeaProjects\\Pkmn\\src\\main\\resources\\my_card.txt";
    public static void main(String[] args) throws Exception {

        CardImport cardimport = new CardImport();
        Card card = new Card();
        card = cardimport.Fill(filename);
        CardExport.Export(card);
        System.out.println(card.toString());

        PkmnHttpClient pkmnHttpClient = new PkmnHttpClient();

        JsonNode card1 = pkmnHttpClient.getPokemonCard("Blissey", "203");
        System.out.println(card1.toPrettyString());
        System.out.println(card1.findValues("attacks")
                .stream()
                .map(JsonNode::toPrettyString)
                .collect(Collectors.toSet()));

        JsonNode card2 = pkmnHttpClient.getPokemonCard("Chansey", "113");
        System.out.println(card2.toPrettyString());
        System.out.println(card2.findValues("attacks")
                .stream()
                .map(JsonNode::toPrettyString)
                .collect(Collectors.toSet()));
//        Card Z = CardImport.SImport("C:\\Users\\Пользователь\\IdeaProjects\\Pkmn\\src\\main\\resources\\Ninetales.crd");
//        System.out.printf(Z.toString());
    }
}