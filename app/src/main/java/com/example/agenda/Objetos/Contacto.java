package com.example.agenda.Objetos;

public class Contacto {

  String id_contacto, uid_contacto, nombres, apellidos, correo, telefono, fec_nacimiento, edad, institucion,
    direccion, imagen;

  public Contacto(){

  }

  public Contacto(String id_contacto, String uid_contacto, String nombres, String apellidos, String correo,
                  String telefono, String fec_nacimiento, String edad, String institucion, String direccion, String imagen) {
    this.id_contacto = id_contacto;
    this.uid_contacto = uid_contacto;
    this.nombres = nombres;
    this.apellidos = apellidos;
    this.correo = correo;
    this.telefono = telefono;
    this.fec_nacimiento = fec_nacimiento;
    this.edad = edad;
    this.institucion = institucion;
    this.direccion = direccion;
    this.imagen = imagen;
  }

  public String getId_contacto() {
    return id_contacto;
  }

  public void setId_contacto(String id_contacto) {
    this.id_contacto = id_contacto;
  }

  public String getUid_contacto() {
    return uid_contacto;
  }

  public void setUid_contacto(String uid_contacto) {
    this.uid_contacto = uid_contacto;
  }

  public String getNombres() {
    return nombres;
  }

  public void setNombres(String nombres) {
    this.nombres = nombres;
  }

  public String getApellidos() {
    return apellidos;
  }

  public void setApellidos(String apellidos) {
    this.apellidos = apellidos;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo= correo;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getFec_nacimiento() {
    return fec_nacimiento;
  }

  public void setFec_nacimiento(String fec_nacimiento) {
    this.fec_nacimiento = fec_nacimiento;
  }


  public String getEdad() {
    return edad;
  }

  public void setEdad(String edad) {
    this.edad = edad;
  }

  public String getInstitucion() {
    return institucion;
  }

  public void setInstitucion(String institucion) {
    this.institucion = institucion;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getImagen() {
    return imagen;
  }

  public void setImagen(String imagen) {
    this.imagen = imagen;
  }
}
