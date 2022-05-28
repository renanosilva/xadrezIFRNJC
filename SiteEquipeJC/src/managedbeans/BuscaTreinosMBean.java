package managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import dao.Database;
import dao.TreinoDAO;
import dominio.EventoTorneio;
import dominio.Treino;
import uteis.MetodosUteis;

@SuppressWarnings("serial") /*Parar de exibir falsos erros*/
@ManagedBean
@SessionScoped
public class BuscaTreinosMBean {
	
	/** Armazena os usu�rios encontrados no banco de acordo com os par�metros de busca. */
	private List<Treino> treinosEncontrados;
	
	/** Permite o acesso ao banco. */
	private TreinoDAO dao;
	
	/** Inicializa��o do MBean */
	@PostConstruct/*Construtor para instanciar alguns objetos*/
	private void init() {
		treinosEncontrados = new ArrayList<>();
		
		dao = new TreinoDAO();
	}
	
	/** Entra na pagina de busca de usu�rios */
	public String entrarBuscarTreinos(){
		return buscar();
	}
	
	/** Realiza a busca de usu�rios no banco. */
	public String buscar(){
		dao = new TreinoDAO();
		
		treinosEncontrados = dao.buscarTreino();
			
		return "/eventos/busca_treino.xhtml";
	}
	
	public String deletar(Treino t) {
		EntityManager em = Database.getInstance().getEntityManager();
		
		try {
			//Iniciando transa��o com o banco de dados
			em.getTransaction().begin();
			
			dao.remover(t);
			
			//Transa��o confirmada
			em.getTransaction().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			
			if (em.getTransaction().isActive())
				//Como ocorreu erro, a transa��o n�o ser� confirmada
				em.getTransaction().rollback();
		}
		
		return buscar();

		
	}

	public List<Treino> getTreinosEncontrados() {
		return treinosEncontrados;
	}

	public void setTreinosEncontrados(List<Treino> treinosEncontrados) {
		this.treinosEncontrados = treinosEncontrados;
	}

	public TreinoDAO getDao() {
		return dao;
	}

	public void setDao(TreinoDAO dao) {
		this.dao = dao;
	}

	
}
