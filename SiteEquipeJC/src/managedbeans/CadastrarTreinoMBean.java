package managedbeans;



import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import dao.Database;
import dominio.Treino;
import dominio.Usuario;
import uteis.MetodosUteis;
import uteis.UsuarioUtil;
import uteis.ValidadorUtil;

@ManagedBean
@SessionScoped
public class CadastrarTreinoMBean {
	private Treino treino;
	private Usuario usuarioTreino;

	public CadastrarTreinoMBean() {
		treino = new Treino();
		usuarioTreino=new Usuario();
		
		
	}

	/** Entra na tela de edi��o de usu�rios. */
	public String entrarEdicaoTreinos(Treino treino) {
		this.treino = treino; // o livro a ser editado sera o recebido pelo
								// parametro
		return "/eventos/CadastrarTreino.xhtml";
	}

	public String cadastrar() {
		boolean erro = false;

		if (MetodosUteis.estaVazia(treino.getMinistrantes())) {
			MetodosUteis.addMensagem("Campo Ministrantes obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(treino.getParticipantes())) {
			MetodosUteis.addMensagem("Campo Participantes obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(treino.getTema())) {
			MetodosUteis.addMensagem("Campo Tema obrigatório!");
			erro = true;
		}
		if (treino.getData() == null) {
			MetodosUteis.addMensagem("Campo Data obrigatório!");
			erro = true;
		}
		if (ValidadorUtil.isEmpty(treino.getTurno())) {
			MetodosUteis.addMensagem("Campo Turno obrigatório!");
			erro = true;
		}

		if (erro)
			return null;
		else {
			// verificando se o usuário anexou foto

			EntityManager gerenciador = Database.getInstance().getEntityManager();
			gerenciador.getTransaction().begin();
			
			
			try {
				if (treino.getId_arquivoTreino() == 0)
					gerenciador.persist(treino);
				else
					gerenciador.merge(treino);

				gerenciador.getTransaction().commit();
				MetodosUteis.addMensagem("Seu cadastro está pronto!");
				

			} catch (Exception e) {
				e.printStackTrace();

				if (gerenciador.getTransaction().isActive()) {
					gerenciador.getTransaction().rollback();
				}
			}

		}

		treino = new Treino();
		return null;
	}

	public Treino getTreino() {
		return treino;
	}

	public void setTreino(Treino treino) {
		this.treino = treino;
	}
	
	public void addParticipante() {
		if(!treino.getParticipantes().contains(usuarioTreino) && usuarioTreino!=null) {
			if (usuarioTreino.getId() == UsuarioUtil.getUsuarioLogado().getId() ||
					UsuarioUtil.getUsuarioLogado().isAdministrador()) {
				treino.getParticipantes().add(usuarioTreino);
				MetodosUteis.addMensagem("Membro inscrito com sucesso!");
			} else 
				MetodosUteis.addMensagem("Somente é possível inscrever a si mesmo no treino.");
		} else {
			MetodosUteis.addMensagem("Usuario já incluso na lista de participantes ou usuário vazio");
		}
	}

	public Usuario getUsuarioTreino() {
		return usuarioTreino;
	}

	public void setUsuarioTreino(Usuario usuarioTreino) {
		
		this.usuarioTreino = usuarioTreino;
	}
	
	public String removerParticipante(Usuario p) {
		treino.getParticipantes().remove(p);
		return "Erro ao remover participante";
	}

}
