package uteis;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * Classe respons�vel por fornecer m�todos �teis para valida��o de dados
 * em geral.
 * 
 * @author Renan
 */
public class ValidadorUtil {
	
	/**
	 * Valida se string  � diferente de null e n�o � vazia.
	 *
	 * @return
	 */
	public static final boolean isEmpty(String s) {
		return (s == null || s.trim().length() == 0);
	}
	
	/**
	 * Valida se um objeto � vazio. O seu funcionamento vai depender do tipo de objeto
	 * passado como par�metro. Se o objeto for nulo, � vazio. Se for uma String, verifica
	 * se n�o � string vazia ou n�o � formada apenas por espa�os. Se for uma cole��o,
	 * verifica se a cole��o est� vazia, etc.
	 *
	 */
	public static final boolean isEmpty(Object o) {
		if (o == null)
			return true;
		if (o instanceof String)
			return isEmpty( (String) o);
		if (o instanceof Number) {
			Number i = (Number) o;
			return (i.intValue() == 0);
		}
		if (o instanceof Object[])
			return ((Object[]) o).length == 0;
		if (o instanceof int[])
			return ((int[]) o).length == 0;
		if (o instanceof Collection<?>)
			return ((Collection<?>) o).size() == 0;
		if (o instanceof Map<?, ?>)
			return ((Map<?, ?>) o).size() == 0;
		return false;
	}
	
	public static final boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}
	
	/** Valida se um email � v�lido. */
	public static boolean validateEmail(String email) {
		if (EmailValidator.getInstance().isValid(email))
			return true;

		return false;
	}

}
