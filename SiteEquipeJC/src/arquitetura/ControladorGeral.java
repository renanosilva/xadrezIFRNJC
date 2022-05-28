package arquitetura;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import auxiliares.MailHelper;
import dominio.Usuario;
import uteis.UsuarioUtil;


/**
 * Classe que implementa m�todos comuns e �teis aos MBeans. Portanto,
 * deve ser estendido por eles.
 *  
 * @author Renan
 */
@SuppressWarnings("serial")
public class ControladorGeral implements Serializable {
	
	/** Adiciona uma mensagem de informa��o a ser exibida para o usu�rio. */
	protected void addMsgInfo(String msg){
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}
	
	/** Adiciona uma mensagem de aviso a ser exibida para o usu�rio. */
	protected void addMsgWarning(String msg){
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null);
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}
	
	/** Adiciona uma mensagem de erro a ser exibida para o usu�rio. */
	protected void addMsgError(String msg){
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}
	
	/**
	 * Tratamento padr�o de exce��es em geral do sistema. Imprime o erro no console,
	 * envia o erro por email para a administra��o do sistema e adiciona uma mensagem
	 * de erro padr�o a ser exibida ao usu�rio.
	 */
	protected void tratamentoErroPadrao(Exception e){
		e.printStackTrace();
		notificarErro(e);
		addMsgError("Ocorreu um erro ao realizar a opera��o. Por favor, entre em contato com a "
				+ "administra��o do sistema, ou tente novamente mais tarde.");
	}
	
	/** Notifica a administra��o do sistema sobre a ocorr�ncia de um erro. */
	protected void notificarErro(Exception e){
		try {
			MailHelper.enviarEmail(MailHelper.EMAIL_SISTEMA, "Erro", getStackTrace(e));
		} catch (AddressException e1) {
			e1.printStackTrace();
		} catch (MessagingException e1) {
			e1.printStackTrace();
		}
	}
	
	/** Transforma uma exce��o em String, para torn�-la leg�vel a humanos. */
	private static String getStackTrace(Throwable t){
		String msg = t.toString() + "<br/>";
		
		if (t.getStackTrace() != null){
			for (StackTraceElement s : t.getStackTrace()){
				msg += s.toString() + "<br/>";
			}
		}
		
		if (t.getCause() != null){
			msg += "Caused by: " + t.getCause().toString() + "<br/>";
			
			if (t.getCause().getStackTrace() != null){
				for (StackTraceElement s : t.getCause().getStackTrace()){
					msg += s.toString() + "<br/>";
				}
			}
		}
		
		
		return msg;
	}
	
	/**
	 * Possibilita o acesso ao HttpServletRequest, ou seja, � requisi��o de acesso
	 * � p�gina atual.
	 */
	public HttpServletRequest getCurrentRequest() {
		return (HttpServletRequest) getExternalContext().getRequest();
	}

	/**
	 * Possibilita o acesso ao HttpServletResponse, ou seja, � resposta a ser enviada
	 * � requisi��o de acesso � p�gina atual.
	 */
	public HttpServletResponse getCurrentResponse() {
		return (HttpServletResponse) getExternalContext().getResponse();
	}

	/**
	 * Possibilita o acesso ao HttpSession, ou seja, � sess�o do sistema,
	 * local da mem�ria onde ficam armazenadas as informa��es atuais.
	 */
	public HttpSession getCurrentSession() {
		return getCurrentRequest().getSession(true);
	}
	
	/**
	 * Acessa o external context do JavaServer Faces
	 **/
	private ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
	
	/** Retorna o usu�rio logado no sistema. */
	public Usuario getUsuarioLogado() {
		return UsuarioUtil.getUsuarioLogado();
	}
	
	/** Obt�m um par�metro que tenha sido enviado atrav�s da URL de uma p�gina. */
	public String getParameter(String param) {
		return getCurrentRequest().getParameter(param);
	}
	
	/** Obt�m um par�metro (n�mero inteiro) que tenha sido enviado atrav�s da URL de uma p�gina. */
	public Integer getParameterInt(String param) {
		return Integer.parseInt(getParameter(param));
	}
	
	public Integer getParameterInt(String param, int padrao) {
		String valor = getParameter(param);
		return valor != null ? Integer.parseInt(valor) : padrao;
	}
	
}
