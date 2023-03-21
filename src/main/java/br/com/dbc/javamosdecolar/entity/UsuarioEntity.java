package br.com.dbc.javamosdecolar.entity;

import br.com.dbc.javamosdecolar.entity.enums.TipoUsuario;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.JOINED)
@SQLDelete(sql = "UPDATE AVIACAO.usuario c SET c.ativo = 0 WHERE c.id_usuario=?")
@Entity(name = "USUARIO")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", allocationSize = 1)
    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "SENHA")
    private String senha;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "TIPO")
    @Enumerated(EnumType.ORDINAL)
    private TipoUsuario tipoUsuario;

    @Column(name = "ATIVO", nullable = false)
    private Boolean ativo;
}