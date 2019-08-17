package com.jdcasas.kachuelo.modelo;
public  class BaseDatos {
      //runhosting
     String IP_Server="http://jdcasasmoviles.atwebpages.com";//localhost
   //  String IP_Server="http://"+"192.168.56.1";//en red
    String URL="" ;
    public BaseDatos(){
     URL = IP_Server+"/serverTabla.php";
            System.out.println("Estoy usando tablas");
    }
    public BaseDatos(int id){
    URL = IP_Server+"/acces.php";
        System.out.println("Estoy usando tablas acceso");
    }

    public String accesoLoginKachuelo(String tabla,String usuario,String password) {
        tabla=replaceEneEsp(tabla);
        usuario=replaceEneEsp(usuario);
        password=replaceEneEsp(password);
        URL = URL+"?operacion=accesoLoginKachuelo&tabla="+tabla+"&usuario="+usuario+"&password="+password;
        System.out.println("URL login kachueloo\n : " + URL);
        return URL;
    }
///////////////////////////////////////carga distritos y provincias/////////////////////////////
    public String listarDepasProvincias(String departamento) {
        departamento=departamento.toLowerCase();
        departamento=replaceEneEsp(departamento);
        System.out.println("URL LISTAR DEPARTAMENTOS  PROVINCIAS\n : " + URL + "?operacion=listarDepasProvincias&departamento="+departamento);
        return URL + "?operacion=listarDepasProvincias&departamento="+departamento;
    }

    public String listaDepasDistritos(String departamento,String provincia) {
        departamento=departamento.toLowerCase();
        departamento=replaceEneEsp(departamento);
        provincia=replaceEneEsp(provincia);
        //URL = URL + "?operacion=listaDepasDistritos&departamento="+departamento+"&provincia="+provincia;
        System.out.println("URL LISTAR DEPARTAMENTOS SUS DISTRITOS\n : " + URL + "?operacion=listaDepasDistritos&departamento="+departamento+"&provincia="+provincia);
        return URL + "?operacion=listaDepasDistritos&departamento="+departamento+"&provincia="+provincia;
    }

    /////////////SPINNER //////////
    public String spOpciones(String tabla,String campo) {
        tabla =tabla.toLowerCase();
        tabla=replaceEneEsp(tabla);
        campo=replaceEneEsp(campo);
        System.out.println("URL SPINNER OPCIONES\n : " + URL + "?operacion=spOpciones&tabla="+tabla+"&campo="+campo);
        return URL + "?operacion=spOpciones&tabla="+tabla+"&campo="+campo;
    }

    /////////////LIST VIEW //////////
    public String lwBuscar(String departamento,String valor1,String valor2,String valor3,String campo1,String campo2,String campo3) {
        departamento =departamento.toLowerCase();
        departamento=replaceEneEsp(departamento);
        valor1=replaceEneEsp(valor1);
        valor2=replaceEneEsp(valor2);
        valor3=replaceEneEsp(valor3);
        campo1=replaceEneEsp(campo1);
        campo2=replaceEneEsp(campo2);
        campo3=replaceEneEsp(campo3);
        URL = URL + "?operacion=lwBuscar&departamento="+departamento+"&campo1="+campo1+"&campo2="+campo2+"&campo3="+campo3+"&valor1="+valor1+"&valor2="+valor2+"&valor3="+valor3;
        System.out.println("URL LIST VIEW  OPCIONES\n : " + URL);
        return URL;
    }

    /////////////////////INSERTANDO USUARIOS PUBLICOS////////////////////////
    public String insertarUsuariosKachuelo(String tabla,String[] datos) {
        tabla =tabla.toLowerCase();
        tabla=replaceEneEsp(tabla);
        URL = URL + "?operacion=insertarUsuariosKachuelo&tabla="+tabla;
        for(int i=0;i<datos.length;i++)
        {
            datos[i]=replaceEneEsp(  datos[i]);
            System.out.println("datos :  "+datos[i] );
            URL = URL +"&datos"+i+"=" +datos[i];;
        }
        System.out.println("URL INSERTAR insertarUsuariosKachuelo: " + URL);
        return URL;
    }

    /////////////////////INSERTANDO EMPLEOS////////////////////////
    public String insertarPublicarEmpleo(String tabla,String[] datos) {
        tabla =tabla.toLowerCase();
        tabla=replaceEneEsp(tabla);
        URL = URL + "?operacion=insertarPublicarEmpleo&tabla="+tabla;
        for(int i=0;i<datos.length;i++)
        {
            datos[i]=replaceEneEsp(  datos[i]);
            System.out.println("datos :  "+datos[i] );
            URL = URL +"&datos"+i+"=" +datos[i];;
        }
        System.out.println("URL INSERTAR insertarPublicarEmpleo: " + URL);
        return URL;
    }
    /////////////////////INSERTANDO  PROYECTOS////////////////////////
    public String insertarProyectos(String tabla,String[] datos) {
        tabla =tabla.toLowerCase();
        tabla=replaceEneEsp(tabla);
        URL = URL + "?operacion=insertarProyectos&tabla="+tabla;
        for(int i=0;i<datos.length;i++)
        {
            datos[i]=replaceEneEsp(  datos[i]);
            System.out.println("datos :  "+datos[i] );
            URL = URL +"&datos"+i+"=" +datos[i];;
        }
        System.out.println("URL INSERTAR insertarProyectos: " + URL);
        return URL;
    }
    /////////////////////////GEOLOCALIZAR CERCA A TI/////////////////////////////////
    public String buscarCercaTiEmpleados(String tabla,String campo,String hcentro,String kcentro,String oficio) {
        hcentro= replaceEneEsp(hcentro);
        kcentro= replaceEneEsp(kcentro);
        oficio= replaceEneEsp(oficio);
        tabla= replaceEneEsp(tabla);
        campo= replaceEneEsp(campo);
        URL = URL + "?operacion=buscarCercaTiEmpleados&hcentro=" +hcentro+"&kcentro=" +kcentro+"&oficio=" +oficio+"&tabla=" +tabla+"&campo=" +campo;
        System.out.println("URL EMPLEADOS  CERCA A TI : " + URL);
        return URL;
    }
    /////////////BUSCAR EMPRESAS //////////
    public String buscarEmpresas(String tabla,String valor1,String campo1) {
        tabla =tabla.toLowerCase();
        tabla=replaceEneEsp(tabla);
        valor1=replaceEneEsp(valor1);
        campo1=replaceEneEsp(campo1);
        URL = URL + "?operacion=buscarEmpresas&tabla="+tabla+"&campo1="+campo1+"&valor1="+valor1;
        System.out.println("URL BUSCAR EMPRESAS\n : " + URL);
        return URL;
    }
    //NUEVA POSICION
    public String nuevaPosicion(String tabla,String usuario,String coorx,String coory) {
        tabla =tabla.toLowerCase();
        tabla=replaceEneEsp(tabla);
        usuario=replaceEneEsp(usuario);
        coorx=replaceEneEsp(coorx);
        coory=replaceEneEsp(coory);
        URL = URL + "?operacion=nuevaPosicion&tabla="+tabla+"&usuario=" +usuario+"&coorx=" +coorx+"&coory=" +coory;
        System.out.println("URL CAMBIAR COORDENADAS : " + URL);
        return URL;
    }
    /////////////LLENAR PROYECTOS //////////
    public String llenarProyectos() {
        System.out.println("URL  LLENAR PROYECTOS\n : " + URL + "?operacion=spOpciones");
        return URL + "?operacion=llenarProyectos";
    }
    //////////////////VITACORA///////////////
    public String vitacora(String creadoPor) {
        creadoPor=replaceEneEsp(creadoPor);
        System.out.println("URL   vitacora\n : " + URL + "?operacion=vitacora&creadoPor="+creadoPor);
        return URL + "?operacion=vitacora&creadoPor="+creadoPor;
    }

    public String replaceEneEsp(String palabra){
        palabra=palabra.replace(" ","%20");
        palabra=palabra.replace("Ñ", "%D1");
        palabra=palabra.replace("ñ", "%F1");
        palabra=palabra.replace("ú", "%FA");
        palabra=palabra.replace("ó", "%F3");
        palabra=palabra.replace("í", "%ED");
        palabra=palabra.replace("é", "%E9");
        palabra=palabra.replace("á", "%E1");
        palabra=palabra.replace("/", "%2F");
        palabra=palabra.replace("@", "%40");
        palabra=palabra.replace("	", "%09");
        palabra=palabra.replace("Á", "%C1");
        palabra=palabra.replace("É", "%C9");
        palabra=palabra.replace("Í", "%CD");
        palabra=palabra.replace("Ó", "%D3");
        palabra=palabra.replace("Ú", "%DA");
        return palabra;
    }
}
