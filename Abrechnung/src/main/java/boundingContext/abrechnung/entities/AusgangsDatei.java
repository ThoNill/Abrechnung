package boundingContext.abrechnung.entities;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import boundingContext.zahlungen.TypeReference;

@Entity
@Table(name = "AUSGANGSDATEI")
@SequenceGenerator(name = "AUSGANGSDATEI_SEQ", sequenceName = "AUSGANGSDATEI_SEQ")
public class AusgangsDatei  {

    public AusgangsDatei() {
        super();
    }

  
    private long ausgangsDateiId;
    private String filename;
    private int fileArt;
    TypeReference protokoll;
    TypeReference quelle;
    
    private Date angelegt;
    private Date gesendet;
    
    @Basic
    @Column(name = "AUSGANGSDATEIID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUSGANGSDATEI_SEQ")
    public long getAusgangsDateiId() {
        return ausgangsDateiId;
    }
    
    public void setAusgangsDateiId(long ausgangsDateiId) {
        this.ausgangsDateiId = ausgangsDateiId;
    }
    
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public int getFileArt() {
        return fileArt;
    }
    public void setFileArt(int fileArt) {
        this.fileArt = fileArt;
    }
    public Date getAngelegt() {
        return angelegt;
    }
    public void setAngelegt(Date angelegt) {
        this.angelegt = angelegt;
    }
    public Date getGesendet() {
        return gesendet;
    }
    public void setGesendet(Date gesendet) {
        this.gesendet = gesendet;
    }
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="art", column=@Column(name="protokoll_art")),
        @AttributeOverride(name="id", column=@Column(name="protokoll_id"))
    })
    public TypeReference getProtokoll() {
        return protokoll;
    }
    public void setProtokoll(TypeReference protokoll) {
        this.protokoll = protokoll;
    }
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="art", column=@Column(name="quelle_art")),
        @AttributeOverride(name="id", column=@Column(name="quelle_id"))
    })
    public TypeReference getQuelle() {
        return quelle;
    }
    public void setQuelle(TypeReference quelle) {
        this.quelle = quelle;
    }
    
     
}
