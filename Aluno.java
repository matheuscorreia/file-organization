public class Aluno {

    private long matricula;     // 8b
    private String nome;        // 60b
    private String endereco;    // 80b
    private String email;       // 50b
    private short curso;        // 2b
    
    public static final int SIZE = 200;


    public Aluno(
      long matricula,
      String nome,
      String endereco,
      String email,
      short curso
    ) {
        super();
        this.matricula = matricula;
        this.nome = nome;
        this.endereco = endereco;
        this.email = email;
        this.curso = curso;
    }
    
    public Aluno() {
    
    }
    
    public long getMatricula() {
        return this.matricula;
    }

    public void setMatricula(long matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public short getCurso() {
        return this.curso;
    }

    public void setCurso(short curso) {
        this.curso = curso;
    }


    @Override
    public String toString() {
        return
          "Matrícula = "    + matricula +
            ", Nome = "     + nome +
            ", Endereço = " + endereco +
            ", Email = "    + email +
            ", Curso = "    + curso +
            "]";
    }
}
