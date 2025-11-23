#!/bin/bash

# Variables configurables
TELEGRAM_API_URL="https://api.telegram.org/bot"
WEBHOOK_BASE_URL="https://11adbaceaff5.ngrok-free.app/chatbot/webhook-telegram"

# Tokens de los bots
WIREGUARD_CLIENT_TOKEN="8558018850:AAFJxq0be_zpVPi4vGpw45zK6yAUsHGI68E"
WIREGUARD_ADMIN_TOKEN="7996809143:AAEAJ-944ot8FpFmien2SCOX0LDkrn1Y9x4"  # Nota: mismo token que Cortana, ¿es correcto?

# Configurar webhook para Cortana
curl --request GET \
     --url "${TELEGRAM_API_URL}${WIREGUARD_CLIENT_TOKEN}/setWebhook?url=${WEBHOOK_BASE_URL}/${WIREGUARD_CLIENT_TOKEN}"

# Configurar webhook para CortanaAdmin
curl --request GET \
     --url "${TELEGRAM_API_URL}${WIREGUARD_ADMIN_TOKEN}/setWebhook?url=${WEBHOOK_BASE_URL}/${WIREGUARD_ADMIN_TOKEN}"