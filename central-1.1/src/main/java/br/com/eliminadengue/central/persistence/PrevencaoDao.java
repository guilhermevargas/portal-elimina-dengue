package br.com.eliminadengue.central.persistence;

import br.com.eliminadengue.central.model.Endereco;
import br.com.eliminadengue.central.model.Foco;
import br.com.eliminadengue.central.model.PercentualGrupoPrevencao;
import br.com.eliminadengue.central.model.Prevencao;
import static br.com.eliminadengue.central.persistence.EntidadeDao.PREVENCAO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author Guilherme Alves
 */
@Perssiste(entidadeDao = PREVENCAO)
public class PrevencaoDao implements Dao<Prevencao> {

    @Inject
    private Connection connection;

    public PrevencaoDao() {
    }

    @Override
    public void salvar(Prevencao prevencao) {
        if (encontrar(prevencao.getCodigoCelular(),
                prevencao.getFoco().getCodigo()) == null) {
            inserir(prevencao);
        } else {
            atualizar(prevencao);
        }
    }

    @Override
    public void inserir(Prevencao obj) {
        String sql = "insert into PREVENCAO(cod_celular, "
                + "cod_foco, dat_criacao, dat_prazo, end_bairro, end_cidade, "
                + "end_estado) values(?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, obj.getCodigoCelular());
            statement.setInt(2, obj.getFoco().getCodigo());
            statement.setDate(3, new Date(obj.getDataCriacao().getTime()));
            statement.setDate(4, new Date(obj.getDataPrazo().getTime()));
            statement.setString(5, obj.getEndereco().getBairro());
            statement.setString(6, obj.getEndereco().getCidade());
            statement.setString(7, obj.getEndereco().getEstado());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PrevencaoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Salvar: " + obj.toString());
    }

    @Override
    public void atualizar(Prevencao obj) {
        String sql = "update PREVENCAO set dat_prazo = ?, dat_efetuada = ?,"
                + "end_bairro = ?, end_cidade = ?, "
                + "end_estado = ? "
                + "where cod_celular = ? and cod_foco = ? and dat_efetuada is null";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setDate(1, new Date(obj.getDataPrazo().getTime()));
            statement.setDate(2, obj.getDataEfetuada() != null ? new Date(obj.getDataEfetuada().getTime()) : null);
            statement.setString(3, obj.getEndereco().getBairro());
            statement.setString(4, obj.getEndereco().getCidade());
            statement.setString(5, obj.getEndereco().getEstado());
            statement.setInt(6, obj.getCodigoCelular());
            statement.setInt(7, obj.getFoco().getCodigo());

            int linhasAfetadas = statement.executeUpdate();
            System.out.println("Atualizar: " + obj.toString() + "linhasAfetadas: " + linhasAfetadas);
        } catch (SQLException ex) {
            Logger.getLogger(PrevencaoDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void excluir(int idCelular, int idFoco) {
        System.out.println("Excluir: " + idCelular + ", " + idFoco);
    }

    @Override
    public Prevencao encontrar(int idCelular, int idFoco) {

        String sql = "select * from PREVENCAO p "
                + "inner join FOCO f on (f.cod_foco = p.cod_foco)"
                + "where p.cod_celular = ? and p.cod_foco = ? and p.dat_efetuada is null";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idCelular);
            statement.setInt(2, idFoco);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String bairro = result.getString("end_bairro");
                String cidade = result.getString("end_cidade");
                String estado = result.getString("end_estado");

                Endereco endereco = new Endereco(bairro, cidade, estado);

                Integer codigoFoco = result.getInt("cod_foco");
                String nome = result.getString("nom_foco");
                String comoLimpar = result.getString("com_limpar");

                Foco foco = new Foco(codigoFoco, nome, comoLimpar);

                Integer codigoCelular = result.getInt("cod_celular");
                Date dataCriacao = result.getDate("dat_criacao");
                Date dataPrazo = result.getDate("dat_prazo");
                Date dataEfetuada = result.getDate("dat_efetuada");

                Prevencao prevencao = new Prevencao(codigoCelular, foco, dataCriacao, dataPrazo, endereco);
                prevencao.setDataEfetuada(dataEfetuada);

                return prevencao;

            }
        } catch (SQLException ex) {
            Logger.getLogger(PrevencaoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Encontrar: " + idCelular + ", " + idFoco);
        return null;
    }

    @Override
    public List<Prevencao> todos() {
        List<Prevencao> prevencoes = new ArrayList<>();

        String sql = "select * from PREVENCAO p "
                + "inner join FOCO f on (f.cod_foco = p.cod_foco) ";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String bairro = result.getString("end_bairro");
                String cidade = result.getString("end_cidade");
                String estado = result.getString("end_estado");

                Endereco endereco = new Endereco(bairro, cidade, estado);

                Integer codigoFoco = result.getInt("cod_foco");
                String nome = result.getString("nom_foco");
                String comoLimpar = result.getString("com_limpar");

                Foco foco = new Foco(codigoFoco, nome, comoLimpar);

                Integer codigoCelular = result.getInt("cod_celular");
                Date dataCriacao = result.getDate("dat_criacao");
                Date dataPrazo = result.getDate("dat_prazo");
                Date dataEfetuada = result.getDate("dat_efetuada");

                Prevencao prevencao = new Prevencao(codigoCelular, foco, dataCriacao, dataPrazo, endereco);
                prevencao.setDataEfetuada(dataEfetuada);

                prevencoes.add(prevencao);
            }

            System.out.println("Listar todos. Tamanho: " + prevencoes.size());

            return prevencoes;

        } catch (SQLException ex) {
            Logger.getLogger(PrevencaoDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public List<PercentualGrupoPrevencao> parcentualPorMes() {
        List<PercentualGrupoPrevencao> prevencoes = new ArrayList<>();
        PercentualGrupoPrevencao percentPrevencaoPorMes = null;

        String sql = "select * from PREVENCAO_PERCENTUAL_MES";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int mes = result.getInt("NUM_MES");
                String nomeMes = result.getString("NOM_MES");
                double percentualAtrasada = result.getDouble("PER_ATRASADA");
                double percentualEfetuada = result.getDouble("PER_EFETUADA");

                percentPrevencaoPorMes = new PercentualGrupoPrevencao();
                percentPrevencaoPorMes.setMes(mes);
                percentPrevencaoPorMes.setPercentualAtrasada(percentualAtrasada);
                percentPrevencaoPorMes.setPercentualEmDia(percentualEfetuada);
                prevencoes.add(percentPrevencaoPorMes);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PrevencaoDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return prevencoes;
    }

    public List<Prevencao> todosPorFoco(int codFoco) {
        List<Prevencao> prevencoes = new ArrayList<>();

        String sql = "select * from PREVENCAO p "
                + "inner join FOCO f on (f.cod_foco = p.cod_foco) "
                + "where f.cod_foco = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, codFoco);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String bairro = result.getString("end_bairro");
                String cidade = result.getString("end_cidade");
                String estado = result.getString("end_estado");

                Endereco endereco = new Endereco(bairro, cidade, estado);

                Integer codigoFoco = result.getInt("cod_foco");
                String nome = result.getString("nom_foco");
                String comoLimpar = result.getString("com_limpar");

                Foco foco = new Foco(codigoFoco, nome, comoLimpar);

                Integer codigoCelular = result.getInt("cod_celular");
                Date dataCriacao = result.getDate("dat_criacao");
                Date dataPrazo = result.getDate("dat_prazo");
                Date dataEfetuada = result.getDate("dat_efetuada");

                Prevencao prevencao = new Prevencao(codigoCelular, foco, dataCriacao, dataPrazo, endereco);
                prevencao.setDataEfetuada(dataEfetuada);

                prevencoes.add(prevencao);
            }

            System.out.println("Listar foco: " + codFoco + " Tamanho: " + prevencoes.size());

            return prevencoes;

        } catch (SQLException ex) {
            Logger.getLogger(PrevencaoDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public List<Prevencao> todos(Integer codFoco, String estado, String cidade, String bairro) {
        List<Prevencao> prevencoes = new ArrayList<>();

        String sql = "select * from PREVENCAO p "
                + "inner join FOCO f on (f.cod_foco = p.cod_foco) "
                + "where 1 = 1";

        if (codFoco != null) {
            sql += " and f.cod_foco = " + codFoco;
        }

        if (estado != null && !estado.trim().equals("")) {
            sql += " and end_estado = '" + estado + "' ";
        }

        if (cidade != null && !cidade.trim().equals("")) {
            sql += " and end_cidade = '" + cidade + "' ";
        }

        if (bairro != null && !bairro.trim().equals("")) {
            sql += " and end_bairro = '" + bairro + "'";
        }

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                bairro = result.getString("end_bairro");
                cidade = result.getString("end_cidade");
                estado = result.getString("end_estado");

                Endereco endereco = new Endereco(bairro, cidade, estado);

                Integer codigoFoco = result.getInt("cod_foco");
                String nome = result.getString("nom_foco");
                String comoLimpar = result.getString("com_limpar");

                Foco foco = new Foco(codigoFoco, nome, comoLimpar);

                Integer codigoCelular = result.getInt("cod_celular");
                Date dataCriacao = result.getDate("dat_criacao");
                Date dataPrazo = result.getDate("dat_prazo");
                Date dataEfetuada = result.getDate("dat_efetuada");

                Prevencao prevencao = new Prevencao(codigoCelular, foco, dataCriacao, dataPrazo, endereco);
                prevencao.setDataEfetuada(dataEfetuada);

                prevencoes.add(prevencao);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PrevencaoDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Listar todos com clausulas: " + codFoco + " Tamanho: " + prevencoes.size());

        return prevencoes;
    }

    public Set<String> localizacoes(String local) {
        Set<String> locais = new TreeSet<>();

        String sql = "SELECT local FROM LOCALIZACAO where local like ? limit 5";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + local + "%");

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                locais.add(result.getString("local"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PrevencaoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return locais;
    }

}
