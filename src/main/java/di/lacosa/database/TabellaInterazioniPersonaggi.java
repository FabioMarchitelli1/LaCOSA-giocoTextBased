package di.lacosa.database;

import di.lacosa.tipi.RispostaPersonaggio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TabellaInterazioniPersonaggi {


    public static void inserisciInterazionePersonaggio(int id_personaggio, String testo, Integer id_dialogo_risposta, Integer prossimo_nodo) {
        String comando_sql = "INSERT INTO InterazioniPersonaggi (id_personaggio, testo, id_dialogo_risposta, prossimo_nodo ) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnessione();
             PreparedStatement pstmt = conn.prepareStatement(comando_sql)) {

            pstmt.setInt(1, id_personaggio);
            pstmt.setString(2, testo);

            if (id_dialogo_risposta != null) {
                pstmt.setInt(3, id_dialogo_risposta);
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            if (prossimo_nodo != null) {
                pstmt.setInt(4, prossimo_nodo);
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void assegnaProssimoNodo (int idDialogo, int prossimo_nodo) {

        String sql = "UPDATE InterazioniPersonaggi SET prossimo_nodo = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnessione();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, prossimo_nodo);
            stmt.setInt(2, idDialogo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static List<RispostaPersonaggio> getTutteInterazioniPersonaggi() {
        String sql = "SELECT * FROM InterazioniPersonaggi";
        List<RispostaPersonaggio> rispostePersonaggi = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnessione();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int id_personaggio = rs.getInt("id_personaggio");
                int id_dialogo_risposta = rs.getInt("id_dialogo_risposta");
                String testo = rs.getString("testo");
                int prossimo_nodo = rs.getInt("prossimo_nodo");
                //INSERISCIIIII
                RispostaPersonaggio risposta = new RispostaPersonaggio(id,id_personaggio,id_dialogo_risposta,testo,prossimo_nodo);
                rispostePersonaggi.add(risposta);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rispostePersonaggi;

    }
}
