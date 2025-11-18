#!/usr/bin/env bash
set -euo pipefail
base="http://localhost:8080"
notebook=$(curl -s -X POST -H "Content-Type: application/json" -d '{"nome":"Notebook","preco":3500.00,"tipo":"PRODUTO"}' "$base/api/catalogo")
mouse=$(curl -s -X POST -H "Content-Type: application/json" -d '{"nome":"Mouse","preco":99.90,"tipo":"PRODUTO"}' "$base/api/catalogo")
instalacao=$(curl -s -X POST -H "Content-Type: application/json" -d '{"nome":"Instalacao","preco":200.00,"tipo":"SERVICO"}' "$base/api/catalogo")
if command -v jq >/dev/null 2>&1; then
  notebook_id=$(printf '%s' "$notebook" | jq -r '.id')
  mouse_id=$(printf '%s' "$mouse" | jq -r '.id')
  instalacao_id=$(printf '%s' "$instalacao" | jq -r '.id')
  pedido_body=$(jq -n --arg n "$notebook_id" --arg s "$instalacao_id" --arg m "$mouse_id" '{descontoPercentual:10,itens:[{produtoServicoId:$n,quantidade:2},{produtoServicoId:$s,quantidade:1},{produtoServicoId:$m,quantidade:3}]}')
  pedido=$(curl -s -X POST -H "Content-Type: application/json" -d "$pedido_body" "$base/api/pedidos")
  pedido_id=$(printf '%s' "$pedido" | jq -r '.id')
  add=$(curl -s -X POST -H "Content-Type: application/json" -d "{\"produtoServicoId\":\"$mouse_id\",\"quantidade\":1}" "$base/api/pedidos/$pedido_id/itens")
  extra_id=$(printf '%s' "$add" | jq -r '.itens[] | select(.produtoServicoId=="'"$mouse_id"'" and .quantidade==1) | .id')
  upd=$(curl -s -X PUT -H "Content-Type: application/json" -d "{\"produtoServicoId\":\"$mouse_id\",\"quantidade\":2}" "$base/api/pedidos/$pedido_id/itens/$extra_id")
  final=$(curl -s "$base/api/pedidos/$pedido_id")
  printf '%s
' "$final"
else
  curl -s "$base/api/catalogo"
fi