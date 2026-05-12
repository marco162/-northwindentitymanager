/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progetto_finale;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import java.sql.Types;

@Entity
@Table(name="Categories")
public class Catalogo {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CategoryID", nullable=false)
	public int CategoryID;
    @Column(name="CategoryName", nullable=false)
    public String CategoryName;
    @Column(name="Description")
    public String Description;
    
    @Lob // Indica che il campo è un Large Object
    @Column(name = "Picture", columnDefinition="BLOB")
    @JdbcTypeCode(Types.BINARY) 
    private byte[] Picture; // Oppure java.sql.Blob
    
    public Catalogo()
    {
    }
    
	public Catalogo(String CategoryName, String Description, byte[] Picture) {
		super();
		this.CategoryName = CategoryName;
		this.Description = Description;
                this.Picture = Picture;
	}

	public Catalogo(int CategoryID, String CategoryName, String Description, byte[] Picture) {
		super();
                this.CategoryID = CategoryID;
		this.CategoryName = CategoryName;
		this.Description = Description;
                this.Picture = Picture;
	}

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int CategoryID) {
        this.CategoryID = CategoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String CategoryName) {
        this.CategoryName = CategoryName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public byte[] getPicture() {
        return Picture;
    }

    public void setPicture(byte[] Picture) {
        this.Picture = Picture;
    }
	
}




