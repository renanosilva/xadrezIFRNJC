package managedbeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

import dao.Database;
import dominio.Arquivo;
import dominio.EventoTorneio;
import dominio.Usuario;
import uteis.MetodosUteis;
import uteis.UsuarioUtil;

@ManagedBean
@SessionScoped
public class CadastrarEventoTorneioMBean {
	private EventoTorneio evento;
	private Usuario usuarioTorneio;
	
	
	public Usuario getUsuarioTorneio() {
		return usuarioTorneio;
	}

	public void setUsuarioTorneio(Usuario usuarioTorneio) {
		this.usuarioTorneio = usuarioTorneio;
	}

	public CadastrarEventoTorneioMBean(){
		evento = new EventoTorneio();
		usuarioTorneio=new Usuario();
	}
	
	public String entrarEdicaoEventoTorneio(EventoTorneio evento) {
		this.evento = evento; // o usu�rio a ser editado ser� o recebido pelo
								// par�metro
		return "/eventos/CadastrarEventoTorneio.xhtml";
	}
	
	public String cadastrar(){
		
		boolean erro = false;
		
		if (MetodosUteis.estaVazia(evento.getLocal())) {
			MetodosUteis.addMensagem("Campo Local do Evento obrigatório!");
			erro = true;
		}
		
		if (MetodosUteis.estaVazia(evento.getNome_torneio())) {
			MetodosUteis.addMensagem("Campo Nome do Torneio obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(evento.getData())) {
			MetodosUteis.addMensagem("Campo Data do Torneio obrigatório!");
			erro = true;
		}
		if (erro){
			return null;
		}else{
			
			EntityManager gerenciador = Database.getInstance().getEntityManager();
			gerenciador.getTransaction().begin();
			
			if (evento.getFotoEvento() != null && !MetodosUteis.estaVazia(evento.getFotoEvento().getFileName())
					&& evento.getFotoEvento().getSize() != -1) {

				// Criando uma entidade Arquivo
				Arquivo arq = new Arquivo();
				arq.setNome(evento.getFotoEvento().getFileName());
				arq.setBytes(evento.getFotoEvento().getContents());

				gerenciador.persist(arq);

				evento.setIdFoto(arq.getId());
			}
			
			if (evento.getId_EventoTorneio() == 0)
				gerenciador.persist(evento);
			else
				gerenciador.merge(evento);
			
			gerenciador.getTransaction().commit();
			MetodosUteis.addMensagem("Seu cadastro está pronto!");
			
		}
		evento = new EventoTorneio();
		return null;
	}
	
	public void addParticipante() {
		if (!evento.getParticipantes().contains(usuarioTorneio) && usuarioTorneio != null) {
			if (usuarioTorneio.getId() == UsuarioUtil.getUsuarioLogado().getId() ||
					UsuarioUtil.getUsuarioLogado().isAdministrador()) {
				evento.getParticipantes().add(usuarioTorneio);
				MetodosUteis.addMensagem("Membro inscrito com sucesso!");
			} else 
				MetodosUteis.addMensagem("Somente é possível inscrever a si mesmo no torneio.");
		} else {
			MetodosUteis.addMensagem("Usuário já incluso na lista de participantes ou usuário vazio");
		}
	}
	
	
	public EventoTorneio getEvento() {
		return evento;
	}
	public void setEvento(EventoTorneio evento) {
		this.evento = evento;
	}
	public String removerParticipante(Usuario p) {
		evento.getParticipantes().remove(p);
		return "Erro ao remover participante";
	}

	
	
	
	
}