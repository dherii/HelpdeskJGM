export interface Chamado {
    id?:                any;
    dataAbertura?:      string;
    dataFechamento?:    string;
    prioridade:         string; // Vamos tratar como string para facilitar os Selects
    status:             string;
    titulo:             string;
    observacoes:        string;
    tecnico:            any;    // ID do técnico
    cliente:            any;    // ID do cliente
    nomeTecnico:        string; // Apenas para exibição na lista
    nomeCliente:        string; // Apenas para exibição na lista
}