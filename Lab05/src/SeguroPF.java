import java.time.LocalDate;
import java.util.ArrayList;

public class SeguroPF extends Seguro
{
    private Veiculo veiculo;
    private ClientePF cliente;

    public SeguroPF(int id, LocalDate dataInicio, LocalDate dataFim, Seguradora seguradora, ArrayList<Sinistro> listaSinistros, ArrayList<Condutor> listaCondutores, int valorMensal, Veiculo veiculo, ClientePF cliente) 
    {
        super(id, dataInicio, dataFim, seguradora, listaSinistros, listaCondutores, valorMensal);
        this.veiculo = veiculo;
        this.cliente = cliente;
    }

    public Veiculo getVeiculo() 
    {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) 
    {
        this.veiculo = veiculo;
    }

    public ClientePF getCliente() 
    {
        return cliente;
    }

    public void setCliente(ClientePF cliente) 
    {
        this.cliente = cliente;
    }

    public void desautorizarCondutor(Condutor condutor)
    {
        super.getListaCondutores().remove(condutor);
    }

    public void autorizarCondutor(Condutor condutor)
    {
        super.getListaCondutores().add(condutor);
    }

    public boolean gerarSinistro(Sinistro sinistro)
    {
        // Stub
        return false;
    }
    
    public int calculaQtdVeiculos()
    {
        int qtdVeiculos = 0;
        String seguradora = super.getSeguradora().getNome();

        for (Veiculo veiculo: cliente.getListaVeiculos())
            if (veiculo.getNomeSeguradora().equals(seguradora))
                qtdVeiculos++;

        return qtdVeiculos;
    }

    public int calculaQtdSinistrosCliente()
    {
        // Não está certo! Não estou entendendo como os sinistros se relacionam a um cliente específico
        int qtdSinistros = 0;

        for (Sinistro sinistro: super.getListaSinistros())
        {
            Seguradora seguradora = sinistro.getSeguro().getSeguradora();

            if (seguradora.getListaClientes().contains(cliente))
                qtdSinistros++;
        }

        return qtdSinistros;
    }

    public int calculaQtdSinistrosCondutor(Condutor condutor)
    {
        int qtdSinistros = 0;

        for (Sinistro sinistro: condutor.getListaSinistros())
            if (sinistro.getSeguro().equals(this))
                qtdSinistros++;

        return qtdSinistros;
    }

    public double calcularValor(Condutor condutor)
    {
        // Foi feito para cliente!
        double fator_idade = 1.0, valor_base = CalcSeguro.VALOR_BASE.getValor();
        int idade = cliente.calculaIdade();
        int qtdVeiculos, qtdSinistrosCliente, qtdSinistrosCondutor;
        qtdVeiculos = calculaQtdVeiculos();
        qtdSinistrosCliente = calculaQtdSinistrosCliente();
        qtdSinistrosCondutor = calculaQtdSinistrosCondutor(condutor);

        if (idade < 30)
            fator_idade = CalcSeguro.FATOR_30_MENOS.getValor();
        else if (30 <= idade && idade <= 60)
            fator_idade = CalcSeguro.FATOR_30_60.getValor();
        else if (idade > 60)
            fator_idade = CalcSeguro.FATOR_60_MAIS.getValor();
        
        return valor_base * fator_idade * (1 + 1/(qtdVeiculos + 2)) * (2 + qtdSinistrosCliente/10) * (5 + qtdSinistrosCondutor/10);
    }
}