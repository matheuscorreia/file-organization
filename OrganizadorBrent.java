import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

class OrganizadorBrent implements IFileOrganizer {
  FileChannel channel;
  
  public OrganizadorBrent(String fileName) throws FileNotFoundException {
    File file = new File(fileName);
    RandomAccessFile rf = new RandomAccessFile(file, "rw");
    channel = rf.getChannel();
  }
  
  private int brentStep(long matric) {
    int p = 120000017;
    return (int) (matric % (p - 2) + 1);
  }
  
  private int hash(long matric) {
    return (int) matric;
  }
  
  private ByteBuffer alunoBufferAt(int pos) {
    long bytePosition = pos * Aluno.SIZE;
    ByteBuffer aluno = ByteBuffer.allocate(Aluno.SIZE);
    try {
      channel.read(aluno, bytePosition);
    } catch (IOException e) {
      e.printStackTrace();
    }
    aluno.position(0);
    return aluno;
  }
  
  private boolean isRegistryEmpty(int pos) {
    ByteBuffer aluno = this.alunoBufferAt(pos);
    return aluno.getLong() == 0;
  }
  
  private void insertRecordInto(ByteBuffer record, int pos) {
    try {
      channel.write(record, pos * Aluno.SIZE);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public void addAluno(Aluno p) {
    // Legend for comments
    // PPC = Primary Probe Chain
    // SPC = Secondary Probe Chain
    
    ByteBuffer aluno = Conversor.toByteBuffer(p);
    
    int hash = this.hash(p.getMatricula());
    
    // if home adress is empty set record
    if (isRegistryEmpty(hash)) {
      this.insertRecordInto(aluno, hash);
    } else {
      
      // Calculates PPC incrementor, and computes next possible position
      int ppcStep = this.brentStep(p.getMatricula());
      int nextPos = hash + ppcStep;
      
      // PPC counter
      int s = 2;
      
      while (!isRegistryEmpty(nextPos)) {
        // if it's the home adress, stop, the table is full
        if (nextPos == hash) {
          System.err.println("full table");
          return;
        }
        // if the record is the same as the one being inserted, stop, duplicate record
        ByteBuffer alu = this.alunoBufferAt(nextPos);
        if (alu.getLong() == p.getMatricula()) {
          System.err.println("duplicate record");
          return;
        }
        // Compute next step
        nextPos += ppcStep;
        
        // increment PPC counter
        s++;
      }
      
      // PPC reader counter
      int i = 1;
      // SPC reader counter
      int j = 1;
      
      while (i + j < s) {
        int ppcIndex = hash + ((i-1) * ppcStep);
  
        ByteBuffer ppcAluno = this.alunoBufferAt(ppcIndex);
  
        int spcStep = this.brentStep(Conversor.toAluno(ppcAluno).getMatricula());
        
        int spcIndex = ppcIndex + (j * spcStep);
        
        if(this.isRegistryEmpty(spcIndex)){
          this.insertRecordInto(ppcAluno, spcIndex);
          this.insertRecordInto(aluno, ppcIndex);
          System.err.println("Insertion successful");
          return;
        }else{
          // vary i and j
          i++;
          j--;
          
          if(j <= 0){
            j = i;
            i = 1;
          }
        }
        
      }
      
      this.insertRecordInto(aluno, s);
      System.err.println("Insertion successful");
    }
    
  }
  
  @Override
  public Aluno getAluno(long matric) {
    return null;
  }
  
  @Override
  public Aluno delAluno(long matric) {
    return null;
  }
}