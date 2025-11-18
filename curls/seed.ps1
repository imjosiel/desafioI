$ErrorActionPreference = 'Stop'
$base = 'http://localhost:8080'

$p1 = Invoke-RestMethod -Method Post -Uri "$base/api/catalogo" -ContentType 'application/json' -Body (@{nome='Notebook';preco=3500.00;tipo='PRODUTO'} | ConvertTo-Json)
$p2 = Invoke-RestMethod -Method Post -Uri "$base/api/catalogo" -ContentType 'application/json' -Body (@{nome='Mouse';preco=99.90;tipo='PRODUTO'} | ConvertTo-Json)
$s1 = Invoke-RestMethod -Method Post -Uri "$base/api/catalogo" -ContentType 'application/json' -Body (@{nome='Instalacao';preco=200.00;tipo='SERVICO'} | ConvertTo-Json)

$req = @{descontoPercentual=10;itens=@(@{produtoServicoId=$p1.id;quantidade=2}, @{produtoServicoId=$s1.id;quantidade=1}, @{produtoServicoId=$p2.id;quantidade=3})} | ConvertTo-Json
$pedido = Invoke-RestMethod -Method Post -Uri "$base/api/pedidos" -ContentType 'application/json' -Body $req

$addItem = Invoke-RestMethod -Method Post -Uri "$base/api/pedidos/$($pedido.id)/itens" -ContentType 'application/json' -Body (@{produtoServicoId=$p2.id;quantidade=1} | ConvertTo-Json)
$extraId = ($addItem.itens | Where-Object { $_.produtoServicoId -eq $p2.id -and $_.quantidade -eq 1 }).id

$updItem = Invoke-RestMethod -Method Put -Uri "$base/api/pedidos/$($pedido.id)/itens/$extraId" -ContentType 'application/json' -Body (@{produtoServicoId=$p2.id;quantidade=2} | ConvertTo-Json)

$final = Invoke-RestMethod -Method Get -Uri "$base/api/pedidos/$($pedido.id)"

[PSCustomObject]@{
  Produto1 = $p1
  Produto2 = $p2
  Servico1 = $s1
  PedidoCriado = [PSCustomObject]@{Id=$pedido.id;TotalProdutos=$pedido.totalProdutos;TotalServicos=$pedido.totalServicos;TotalDesconto=$pedido.totalDesconto;TotalFinal=$pedido.totalFinal}
  PedidoFinal = [PSCustomObject]@{Id=$final.id;TotalProdutos=$final.totalProdutos;TotalServicos=$final.totalServicos;TotalDesconto=$final.totalDesconto;TotalFinal=$final.totalFinal;Itens=$final.itens}
} | ConvertTo-Json -Depth 5 -Compress