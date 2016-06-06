package br.jus.trt12.paulopinheiro.ldaputil.usuarios;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPException;
import java.io.UnsupportedEncodingException;

public class Usuario {
    private boolean autenticado = false;
    private static int ldapVersion   = LDAPConnection.LDAP_V3;
    private static int ldapPort      = LDAPConnection.DEFAULT_PORT;
    private static String trt12LdapHost = "ldap.trt12.jus.br";

    
    public void autenticar(String usuario, String senha) {
        autenticar(usuario,senha,trt12LdapHost,ldapPort,ldapVersion);
    }

    public void autenticar(String usuario, String senha, String host) {
        autenticar(usuario,senha,host,ldapPort,ldapVersion);
    }

    public void autenticar(String usuario,String senha,String host,int porta, int versaoLdap) {
        setAutenticado(false);
        LDAPConnection conn = new LDAPConnection();
        String dn = dnUsuario(usuario);
        try {
            //conectando ao server
            conn.connect(host, porta);
            //fazendo o "bind" para ver se credenciais conferem
            conn.bind(versaoLdap, dn, senha.getBytes("UTF8") );
            if (conn.getAuthenticationDN() != null) setAutenticado(true);
            conn.disconnect();
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Problemas com codificação: " + ex.getMessage());
        } catch (LDAPException ex) {throw new RuntimeException("Problemas com LDAP: " + ex.getMessage());}
    }

    private static String dnUsuario(String usuario) {
        return "uid=" + usuario + ",ou=Servidores,ou=Pessoas,dc=trt12,dc=gov,dc=br";
    }

    public boolean isAutenticado() {
        return autenticado;
    }

    private void setAutenticado(boolean autenticado) {
        this.autenticado = autenticado;
    }
}
