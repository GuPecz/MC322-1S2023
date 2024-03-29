import java.util.ArrayList;

public class Seguradora
{
    private final String cnpj;
    private String nome;
    private String telefone;
    private String endereco;
    private String email;
    private ArrayList<Cliente> listaClientes;
    private ArrayList<Seguro> listaSeguros;
    
    public Seguradora(String cnpj, String nome, String telefone, String endereco, String email, ArrayList<Cliente> listaClientes, ArrayList<Seguro> listaSeguros) 
    {
        this.cnpj = cnpj;
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
        this.email = email;
        this.listaClientes = listaClientes;
        this.listaSeguros = listaSeguros;
    }
    
    public String getCnpj() 
    {
        return cnpj;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getTelefone()
    {
        return telefone;
    }

    public void setTelefone(String telefone)
    {
        this.telefone = telefone;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public String getEndereco()
    {
        return endereco;
    }
    
    public void setEndereco(String endereco)
    {
        this.endereco = endereco;
    }
    
    public ArrayList<Cliente> getListaClientes() 
    {
        return listaClientes;
    }
    
    public void setListaClientes(ArrayList<Cliente> listaClientes) 
    {
        this.listaClientes = listaClientes;
    }
    
    public ArrayList<Seguro> getListaSeguros() 
    {
        return listaSeguros;
    }

    public void setListaSeguros(ArrayList<Seguro> listaSeguros) 
    {
        this.listaSeguros = listaSeguros;
    }

    public boolean cadastrarCliente(Cliente cliente)
    {
        if (listaClientes.contains(cliente))
        {
            System.out.println("ERRO: Cliente já cadastrado!");
            return false;
        }
        else
        {
            System.out.println("Cadastro realizado");
            listaClientes.add(cliente);
            return true;
        }
    }

    public boolean excluirCliente(String documento)
    {
        if (listaClientes.isEmpty() == true)
        {
            System.out.println("ERRO: Não há clientes para serem removidos");
            return false;
        }
        else if (!Validacao.validarCPF(documento) && !Validacao.validarCNPJ(documento))
        {
            System.out.println("ERRO: Documento inválido");
            return false;
        }

        char tipoCliente;
        if (Validacao.validarCPF(documento))
            tipoCliente = 'f';
        else
            tipoCliente = 'j';

        for (Cliente cliente: listaClientes)
            switch (tipoCliente)
            {
                case 'f':
                if (cliente instanceof ClientePF)
                {
                    ClientePF clientePF = (ClientePF)cliente;

                    if (clientePF.getCpf().equals(documento))
                    {
                        listaClientes.remove(clientePF);
                        System.out.println("Cliente " + clientePF.getNome() + " removido");
                        return true;
                    }
                }
                break;

                case 'j':
                if (cliente instanceof ClientePJ)
                {
                    ClientePJ clientePJ = (ClientePJ)cliente;

                    if (clientePJ.getCnpj().equals(documento))
                    {
                        listaClientes.remove(clientePJ);
                        System.out.println("Cliente " + clientePJ.getNome() + " removido");
                        return true;
                    }
                }
            }

        System.out.println("ERRO: Não há cliente com este documento cadastrado");
        return false;
    }

    public boolean gerarSeguro(Seguro seguro)
    {
        if (listaSeguros.contains(seguro))
        {
            System.out.println("ERRO: Seguro já registrado");
            return false;
        }
        else
        {
            listaSeguros.add(seguro);
            return true;
        }
    }

    public boolean cancelarSeguro(Seguro seguro)
    {
        if (!listaSeguros.contains(seguro))
        {
            System.out.println("ERRO: Seguro não registrado");
            return false;
        }
        else
        {
            listaSeguros.remove(seguro);
            return true;
        }
    }

    private ClientePF getClientePorCpf(String cpf)
    {
        for (Cliente cl: listaClientes)
            if (cl instanceof ClientePF)
            {
                ClientePF cliente = (ClientePF)cl;

                if (cliente.getCpf().equals(cpf))
                    return cliente;
            }

        System.out.println("ERRO: Não há cliente cadastrado com esse documento");
        return null;
    }

    private ClientePJ getClientePorCnpj(String cnpj)
    {
        for (Cliente cl: listaClientes)
            if (cl instanceof ClientePJ)
            {
                ClientePJ cliente = (ClientePJ)cl;

                if (cliente.getCnpj().equals(cnpj))
                    return cliente;
            }

        System.out.println("ERRO: Não há cliente cadastrado com esse documento");
        return null;
    }

    public ArrayList<Seguro> getSegurosPorCliente(String documento)
    {
        ArrayList<Seguro> segurosCliente = new ArrayList<Seguro>();

        if (!Validacao.validarCNPJ(documento) && !Validacao.validarCPF(documento))
        {
            System.out.println("ERRO: Documento inválido");
            return null;
        }

        if (Validacao.validarCNPJ(documento))
        {
            ClientePF clientePf = getClientePorCpf(documento);

            for (Seguro seguro: listaSeguros)
            if (seguro instanceof SeguroPF)
            {
                SeguroPF seguroPf = (SeguroPF)seguro;

                if (seguroPf.getCliente().equals(clientePf))
                    segurosCliente.add(seguroPf);
            }
        }
        else if (Validacao.validarCPF(documento))
        {
            ClientePJ clientePj = getClientePorCnpj(documento);

            for (Seguro seguro: listaSeguros)
                if (seguro instanceof SeguroPJ)
                {
                    SeguroPJ seguroPj = (SeguroPJ)seguro;
    
                    if (seguroPj.getCliente().equals(clientePj))
                        segurosCliente.add(seguroPj);
                }
        }

        return segurosCliente;
    }

    public ArrayList<Sinistro> getSinistrosPorCliente(String documento)
    {        
        ArrayList<Sinistro> sinistrosCliente = new ArrayList<Sinistro>();

        if (!Validacao.validarCNPJ(documento) && !Validacao.validarCPF(documento))
        {
            System.out.println("ERRO: Documento inválido");
            return null;
        }

        if (Validacao.validarCPF(documento))
        {
            ClientePF clientePf = getClientePorCpf(documento);

            for (Seguro seguro: listaSeguros)
                if (seguro instanceof SeguroPF)
                {
                    SeguroPF seguroPf = (SeguroPF)seguro;

                    if (seguroPf.getCliente().equals(clientePf))
                        sinistrosCliente.addAll(seguroPf.getListaSinistros());
                }
        }
        else if (Validacao.validarCNPJ(documento))
        {
            ClientePJ clientePj = getClientePorCnpj(documento);

            for (Seguro seguro: listaSeguros)
                if (seguro instanceof SeguroPJ)
                {
                    SeguroPJ seguroPj = (SeguroPJ)seguro;
    
                    if (seguroPj.getCliente().equals(clientePj))
                        sinistrosCliente.addAll(seguroPj.getListaSinistros());
                }
        }

        return sinistrosCliente;
    }
    
    public String listarClientesPorSeguradora()
    {
        int tam = listaClientes.size();
        String clientes = "";
        
        if (tam == 0)
        return "ERRO: Não há clientes cadastrados nesta seguradora";

        for (int i = 0; i < tam; i++)
        {
            Cliente cliente = listaClientes.get(i);
            clientes += "Cliente " + (i + 1);
            
            if (cliente instanceof ClientePF)
            {
                ClientePF clientePF = (ClientePF)cliente;
                clientes += "(PF)" + "\n" + clientePF.toString();
            }
            else
            {
                ClientePJ clientePJ = (ClientePJ)cliente;
                clientes += "(PJ)" + "\n" + clientePJ.toString();
            }
            
            clientes += "\n";
        }
        
        return clientes;
    }
    
    public String listarSegurosPorSeguradora()
    {
        int tam = listaSeguros.size();
        String seguros = "";
        
        if (tam == 0)
        return "ERRO: Não há seguros cadastrados nesta seguradora";
        
        for (int i = 0; i < tam; i++)
        {
            Seguro seguro = listaSeguros.get(i);
            seguros += "Seguro " + (i + 1);
            
            if (seguro instanceof SeguroPF)
            {
                SeguroPF seguroPF = (SeguroPF)seguro;
                seguroPF.setValorMensal(seguroPF.calcularValor());
                seguros += "(PF)" + "\n" + seguroPF.toString();
            }
            else
            {
                SeguroPJ seguroPJ = (SeguroPJ)seguro;
                seguroPJ.setValorMensal(seguroPJ.calcularValor());
                seguros = "(PJ)" + "\n" + seguroPJ.toString();
            }
            
            seguros += "\n";
        }

        return seguros;
    }
    
    public String listarSegurosPorCliente(Cliente cliente)
    {
        int tam = listaSeguros.size();
        String seguros = "Seguradora " + nome + "(CNPJ: " + cnpj + ")\n";
        
        if (tam == 0)
        return "ERRO: Não há seguros cadastrados nesta seguradora";
        
        for (int i = 0; i < tam; i++)
        {
            Seguro seguro = listaSeguros.get(i);
            seguros += "Seguro " + (i + 1);
            
            if (cliente instanceof ClientePF && seguro instanceof SeguroPF)
            {
                ClientePF clientePf = (ClientePF)cliente;
                SeguroPF seguroPf = (SeguroPF)seguro;
                seguroPf.setValorMensal(seguroPf.calcularValor());

                if (seguroPf.getCliente().equals(clientePf))
                    seguros += "\n" + seguroPf.toString();
            }
            else if (cliente instanceof ClientePJ && seguro instanceof SeguroPJ)
            {
                ClientePJ clientePj = (ClientePJ)cliente;
                SeguroPJ seguroPj = (SeguroPJ)seguro;
                seguroPj.setValorMensal(seguroPj.calcularValor());
                
                if (seguroPj.getCliente().equals(clientePj))
                    seguros += "\n" + seguroPj.toString();
            }

            seguros += "\n";
        }
        
        return seguros;
    }

    public Cliente selecionarCliente()
	{
        Cliente cliente;
		
		if (getListaClientes().isEmpty())
		{
			System.out.println("Por favor, cadastre um cliente primeiro");
			cliente = null;
		}
		else
		{
			int opcao;
            
			System.out.println("Selecione um cliente");
			listarClientesPorSeguradora();
			do
			{	
                opcao = Leitura.leInt() - 1;
			} while (!Validacao.validarIndice(opcao, listaClientes));
			cliente = listaClientes.get(opcao);
		}
		
		return cliente;
	}
    
	public Seguro selecionarSeguro()
	{
		Seguro seguro;
		
		if (listaSeguros.isEmpty())
		{
			System.out.println("Por favor, primeiro cadastre um seguro");
			seguro = null;
		}
		else
		{
			int opcao;
			
			System.out.println("Selecione um seguro");
			System.out.println(listarSegurosPorSeguradora());
			do
			{	
				opcao = Leitura.leInt() - 1;
			} while (!Validacao.validarIndice(opcao, listaSeguros));
			seguro = listaSeguros.get(opcao);
		}
		
		return seguro;
	}

    public double calcularReceita()
    {
        double receita = 0.0;
        
        for (Seguro seguro: listaSeguros)
        {
            seguro.setValorMensal(seguro.calcularValor());
            receita += seguro.getValorMensal();
        }
        return receita;
    }
    
    @Override
    public String toString()
    {
        return "Informações da seguradora" + "\nCNPJ: " + cnpj + "\nNome: " + nome
        + "\nTelefone: " + telefone + "\nEndereço: " + endereco + "\nE-mail: " + email
        + "\nClientes: " + listaClientes + "\nSeguros: " + listaSeguros;
    }
}