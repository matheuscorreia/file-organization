import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

class OrganizadorBrent implements IFileOrganizer {
  FileChannel channel;
  int p = 12000017;
  
  public OrganizadorBrent(File file, String permissions) {
    RandomAccessFile rf = null;
    try {
      rf = new RandomAccessFile(file, "rw");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    channel = rf.getChannel();
  }
  
  private int preventTableOverflow(int pos){
    return pos >= p ? pos - p : pos;
  }
  
  private int brentStep(long matric) {
    return (int) (matric % (p - 2) + 1);
  }
  
  private int hash(long matric) {
    int hash =  ((int) matric) % p;
    return hash;
  }
  
  private ByteBuffer alunoBufferAt(int pos) {
    int position = this.preventTableOverflow(pos);
    long bytePosition = position * (long) Aluno.SIZE;
    ByteBuffer aluno = ByteBuffer.allocate(Aluno.SIZE);
    try {
      channel.read(aluno, bytePosition);
    } catch (IOException e) {
      e.printStackTrace();
    }
    aluno.position(0);
    return aluno;
  }
  
  private boolean isBufferReal(ByteBuffer reg){
    reg.position(0);
    long registryMatr = reg.getLong();
    reg.position(0);
    return registryMatr > 0;
  }
  
  private boolean isRegistryEmpty(int pos) {
    int position = this.preventTableOverflow(pos);
    ByteBuffer aluno = this.alunoBufferAt(position);
    return aluno.getLong() == 0;
  }
  
  private void insertRecordInto(ByteBuffer record, int pos) {
    int position = this.preventTableOverflow(pos);
    record.position(0);
    try {
      long bytePosition = position * (long) Aluno.SIZE;
      channel.write(record, bytePosition);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private Aluno deleteAlunoAt(int pos){
    int position = this.preventTableOverflow(pos);
    ByteBuffer bfEmpty = ByteBuffer.allocateDirect(Aluno.SIZE);
    ByteBuffer bfAluno = ByteBuffer.allocate(Aluno.SIZE);
    try {
      long bytePosition = position * (long) Aluno.SIZE;
      
      channel.read(bfAluno, bytePosition);
      
      channel.write(bfEmpty, bytePosition);
      
      return Conversor.toAluno(bfAluno);
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
  @Override
  public void addAluno(Aluno p) {
    // Legend for comments
    // PPC = Primary Probe Chain
    // SPC = Secondary Probe Chain
    
    ByteBuffer aluno = Conversor.toByteBuffer(p);
    long pMatricula = p.getMatricula();
    int hash = this.hash(pMatricula);
    // if home adress is empty set record
    if (isRegistryEmpty(hash)) {
      this.insertRecordInto(aluno, hash);
    } else {
      
      //  ppcInc multiplier
      int i = 1;
      //  spcInc multiplier
      int j = 0;
      
      // first PPC position (Home Adress)
      int ppcPos = hash;
      int ppcInc = this.brentStep(pMatricula);
      
      //  since the first insertion probe is on p(2,0) always, no consideration for the spc is needed
      int spcInc = 0;
      
      //  Computes the first position acessed by the seeker that will traverse the positions.
      //  This will return p(2,0) position
      int seekerProbePos = ppcPos + (i * ppcInc);
      
      ByteBuffer seekerResult = this.alunoBufferAt(seekerProbePos);
      
      while(this.isBufferReal(seekerResult)){
        
        //  vary i and j
        if(j==0){
          j = i;
          i = 0;
        }else{
          j--;
          i++;
          if(j==0)
            i++;
        }
        ByteBuffer ppcAluno = this.alunoBufferAt(ppcPos + (i * ppcInc));
        spcInc = this.brentStep(ppcAluno.getLong());
        seekerProbePos = (ppcPos + (i * ppcInc)) + (j * spcInc);
        seekerResult = this.alunoBufferAt(seekerProbePos);
      }
  
      if(j > 0){  //  spc offset of a registry on the ppc
        
        int ppcPosition = ppcPos + (i * ppcInc);
        
        // gets the aluno at PPC
        ByteBuffer ppcAluno = this.alunoBufferAt(ppcPosition);
        
        this.insertRecordInto(ppcAluno, seekerProbePos);
        
        this.insertRecordInto(aluno, ppcPosition);
        
        System.err.println("Insertion Successful with movement to the SPC");
      }else{      //  directly on ppc
        this.insertRecordInto(aluno, ppcPos + (i * ppcInc));
        System.err.println("Insertion Successful on PPC");
      }
 
    }
    
  }
  
  @Override
  public Aluno getAluno(long matric) {
    
    if(matric <= 0){
      return null;
    }
    
    int hash = this.hash(matric);
    
    int pos = hash;
  
    ByteBuffer seekerResult = this.alunoBufferAt(hash);
    
    if(seekerResult.getLong() == matric) {
      return Conversor.toAluno(seekerResult);
    }else{
      int pcStep = this.brentStep(matric);
      pos += pcStep;
      seekerResult = this.alunoBufferAt(pos);
      
      while(this.isBufferReal(seekerResult)){
        if(seekerResult.getLong() == matric){
          return Conversor.toAluno(seekerResult);
        }else{
          pos += pcStep;
          seekerResult = this.alunoBufferAt(pos);
        }
      }
    }
    
    return null;
  }
  
  @Override
  public Aluno delAluno(long matric) {
    if(matric <= 0){
      return null;
    }
  
    int hash = this.hash(matric);
  
    int pos = hash;
  
    ByteBuffer seekerResult = this.alunoBufferAt(hash);
  
    if(seekerResult.getLong() == matric) {
      // remove
    }else{
      int pcStep = this.brentStep(matric);
      pos += pcStep;
      seekerResult = this.alunoBufferAt(pos);
    
      while(this.isBufferReal(seekerResult)){
        if(seekerResult.getLong() == matric){
          // remove
        }else{
          pos += pcStep;
          seekerResult = this.alunoBufferAt(pos);
        }
      }
    }
  
    return null;
  }
}