package managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

import dao.LivroDAO;
import dao.Database;
import dominio.Arquivo;
import dominio.Livro;
import dominio.RodadaTorneio;
import uteis.MetodosUteis;

@SuppressWarnings("serial") /*Parar de exibir falsos erros*/
@ManagedBean
@SessionScoped
public class BuscaLivrosMBean {
	
	/** Armazena os usu�rios encontrados no banco de acordo com os par�metros de busca. */
	private List<Livro> livrosEncontrados;
	
	/** Permite o acesso ao banco. */
	private LivroDAO dao;
	
	/** Inicializa��o do MBean */
	@PostConstruct/*Construtor para instanciar alguns objetos*/
	private void init() {
		livrosEncontrados = new ArrayList<>();
		
		dao = new LivroDAO();
	}
	
	/** Entra na pagina de busca de usu�rios */
	public String entrarBuscarLivros(){
		return buscar();
	}
	
	/** Realiza a busca de usu�rios no banco. */
	public String buscar(){
		dao = new LivroDAO();
		
		livrosEncontrados = dao.buscarLivro();
			
		return "/material/busca_livro.xhtml";
	}

	public List<Livro> getLivrosEncontrados() {
		return livrosEncontrados;
	}

	public void setLivrosEncontrados(List<Livro> livrosEncontrados) {
		this.livrosEncontrados = livrosEncontrados;
	}

	public LivroDAO getDao() {
		return dao;
	}

	public void setDao(LivroDAO dao) {
		this.dao = dao;
	}
	

	public String deletar(Livro l) {
		EntityManager em = Database.getInstance().getEntityManager();
		
		try {
			//Iniciando transa��o com o banco de dados
			em.getTransaction().begin();
			
			//Verificando se o patrimonio já possui foto anterior para removê-la
			if (l.getIdFoto() != null && l.getIdFoto() != 0) {
				Arquivo foto = dao.encontrarPeloID(l.getIdFoto(), Arquivo.class);
				dao.remover(foto);
			}
			
			dao.remover(l);
			
			//Transa��o confirmada
			em.getTransaction().commit();
			
		} catch (Exception e){
			if(e instanceof  javax.persistence.RollbackException) {
				MetodosUteis.addMensagem("Não é possivel deletar esse livro porque existe uma solicitação pendente");
			}
			e.printStackTrace();
			
			if (em.getTransaction().isActive())
				//Como ocorreu erro, a transa��o n�o ser� confirmada
				em.getTransaction().rollback();
		}
		
		return buscar();

		
	}
	
	


}
