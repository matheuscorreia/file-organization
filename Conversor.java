import java.nio.*;

public class Conversor {

  public static ByteBuffer toByteBuffer(Aluno a){
    //  allocate a buffer, the size of a student
    ByteBuffer aluno = ByteBuffer.allocate(Aluno.SIZE); // p = 0
    
    int p = 0;
    
    aluno.putLong(a.getMatricula());
    p += 8;
    
    aluno.put(a.getNome().getBytes());
    p += 60;
    aluno.position(p);
    
    aluno.put(a.getEndereco().getBytes());
    p += 80;
    aluno.position(p);
    
    aluno.put(a.getEmail().getBytes());
    p += 50;
    aluno.position(p);
    
    aluno.putShort(a.getCurso());
    
    aluno.flip();
    
    return aluno;
  }
  
  public static Aluno toAluno(ByteBuffer buf){
    buf.position(0);
    
    Aluno newAluno = new Aluno();
    byte[] byteHolder;
    
    newAluno.setMatricula(buf.getLong());
  
    byteHolder = new byte[60];
    buf.get(byteHolder);
    newAluno.setNome(new String(byteHolder));
  
    byteHolder = new byte[80];
    buf.get(byteHolder);
    newAluno.setEndereco(new String(byteHolder));
  
    byteHolder = new byte[50];
    buf.get(byteHolder);
    newAluno.setEmail(new String(byteHolder));
  
    newAluno.setCurso(buf.getShort());
    
    buf.position(0);
    
    return newAluno;
  }
}
