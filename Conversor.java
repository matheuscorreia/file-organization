import java.nio.*;

public class Conversor {

  public static ByteBuffer toByteBuffer(Aluno a){
    //  allocate a buffer, the size of a student
    ByteBuffer aluno = ByteBuffer.allocate(Aluno.SIZE); // p = 0
    
    int p = 0;
    
    aluno.putLong(a.getMatricula());
    p += 8;
    
    aluno.put(a.getNome().getBytes());
    aluno.position(p += 60);
    
    aluno.put(a.getEndereco().getBytes());
    aluno.position(p += 80);
    
    aluno.put(a.getEmail().getBytes());
    aluno.position(p += 50);
    
    aluno.putShort(a.getCurso());
    p += 2;
    
    aluno.flip();
    
    return aluno;
  }
  
  public static Aluno toAluno(ByteBuffer buf){
    buf.position(0);
    
    Aluno newAluno = new Aluno();
    
    newAluno.setMatricula(buf.getLong());
  
    newAluno.setNome(buf.get(new byte[60]).toString());
  
    newAluno.setEndereco(buf.get(new byte[80]).toString());
  
    newAluno.setEmail(buf.get(new byte[50]).toString());
  
    newAluno.setCurso(buf.getShort());
    
    buf.position(0);
    
    return newAluno;
  }
}
