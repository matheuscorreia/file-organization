/**
 * Interface genérica que define as operações de organizadores
 * de arquivos de alunos em disco.
 * @author Tarcisio Rocha
 */
public interface IFileOrganizer {
  
  /**
   * Dada uma instância da classe Aluno, este método
   * adiciona os dados da instância em um arquivo seguindo o
   * método de organização de arquivos especificado.
   * @param p Instância da classe Aluno
   */
  public void addAluno(Aluno p);
  
  /**
   * Dado um número de matrícula, este método consulta o arquivo de
   * alunos e devolve uma instância que encapsula
   * aos dados do aluno que contém a matrícula fornecida.
   * @param matric Número de matrícula para a consulta.
   * @return Instância da classe Aluno correspondente
   *         à matrícula fornecida;
   *         Null se a matrícula informada não existe no arquivo.
   */
  public Aluno getAluno(long matric);
  
  /**
   * Dado um número de matrícula, localiza e exclui o registro do
   * arquivo de alunos que corresponde à matrícula
   * fornecida.
   * @param matric Matrícula do aluno a ser excluído.
   * @return Aluno que foi excluído.
   */
  public Aluno delAluno(long matric);
}
