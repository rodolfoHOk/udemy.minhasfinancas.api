package br.com.hioktec.minhasfinancas.model.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios", schema = "financas", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"nome_usuario"}),
		@UniqueConstraint(columnNames = {"email"})
})
/* substituimos todos estes pela anotação @Data exceto @NoArgsConstructor
* @Getter
* @Setter
* @EqualsAndHashCode
* @ToString
* @NoArgsConstructor
* @AllArgsConstructor
*/
@Data
@NoArgsConstructor // dá conflito com o builder que necessita do construtor com todos os argumentos
@AllArgsConstructor // então declaramos este para solucionar
// @Builder retiramos para uso do autoridades para segurança JWT
public class Usuario {
	
	@Id
	@Column(name = "id" )
	@GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY no postgresql usar AUTO para mySQL
	private Long id;
	
	@NotBlank // adicionamos para a validação dos dados do lado do servidor em todos os campos
	@Size(max = 40) // adicionamos para a validação dos dados do lado do servidor em todos os campos
	@Column(name = "nome")
	private String nome;
	
	@NotBlank
	@Size(max = 20)
	@Column(name = "nome_usuario")
	private String nomeUsuario; // adicionamos para segurança JWT
	
	@NaturalId
	@NotBlank
	@Size(max = 40)
	@Email // adicionamos para a validação do lado do servidor
	@Column(name = "email")
	private String email;
	
	@NotBlank
	@Size(max = 100)
	@Column(name = "senha")
	@JsonIgnore
	private String senha;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "autoridades_usuarios",
		schema = "financas",
		joinColumns = @JoinColumn(name = "usuarioId"),
		inverseJoinColumns = @JoinColumn(name = "autoridadeId"))
	private Set<Autoridade> autoridades = new HashSet<Autoridade>(); // adicionamos para segurança JWT

	public Usuario(@NotBlank @Size(max = 40) String nome, @NotBlank @Size(max = 20) String nomeUsuario,
			@NotBlank @Size(max = 40) @Email String email, @NotBlank @Size(max = 100) String senha) {
		this.nome = nome;
		this.nomeUsuario = nomeUsuario;
		this.email = email;
		this.senha = senha;
	}
	
}
	
	/* Removemos pois usaremos o lombok! (projectlombok.org)
	// getters and setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (senha == null) {
			if (other.senha != null)
				return false;
		} else if (!senha.equals(other.senha))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", name=" + name + ", email=" + email + ", senha=" + senha + "]";
	}
}
*/
