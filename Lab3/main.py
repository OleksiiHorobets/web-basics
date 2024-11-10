import logging
import os
from telegram import Update, ReplyKeyboardMarkup, KeyboardButton
from telegram.ext import Updater, CommandHandler, MessageHandler, Filters, CallbackContext
import requests
from dotenv import load_dotenv

load_dotenv()

logging.basicConfig(
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    level=logging.INFO
)
logger = logging.getLogger(__name__)


USER_DATA = {
    'name': 'Oleksii Horobets',
    'group': 'IP-12',
    'position': 'Java Backend Developer',
    'phone': '+123456789',
    'email': 'oleksii@example.com'
}

TELEGRAM_BOT_TOKEN = os.getenv('TELEGRAM_BOT_TOKEN')
GROQ_API_KEY = os.getenv('GROQ_API_KEY')


GROQ_API_ENDPOINT = "https://api.groq.com/openai/v1/chat/completions"

def start(update: Update, context: CallbackContext) -> None:
    reply_keyboard = [
        [KeyboardButton("/student")],
        [KeyboardButton("/it_tech")],
        [KeyboardButton("/contacts")],
        [KeyboardButton("/ai_prompt")]
    ]
    update.message.reply_text(
        "Choose an option:",
        reply_markup=ReplyKeyboardMarkup(reply_keyboard, one_time_keyboard=True)
    )


def student(update: Update, context: CallbackContext) -> None:
    response = f"{USER_DATA['name']}, group {USER_DATA['group']}"
    update.message.reply_text(response)

def it_tech(update: Update, context: CallbackContext) -> None:
    response = USER_DATA['position']
    update.message.reply_text(response)


def contacts(update: Update, context: CallbackContext) -> None:
    response = f"Phone: {USER_DATA['phone']}\nEmail: {USER_DATA['email']}"
    update.message.reply_text(response)


def ai_prompt(update: Update, context: CallbackContext) -> None:
    response = "Please enter your prompt for the AI:"
    context.user_data['expecting_prompt'] = True
    update.message.reply_text(response)

def handle_ai_prompt(update: Update, context: CallbackContext) -> None:
    if context.user_data.get('expecting_prompt'):
        prompt = update.message.text
        context.user_data['expecting_prompt'] = False

        ai_response = call_groq_ai(prompt)
        update.message.reply_text(f"AI Response: {ai_response}")
    else:
        update.message.reply_text("Please choose a valid menu option.")


def call_groq_ai(prompt: str) -> str:
    headers = {
        'Authorization': f'Bearer {GROQ_API_KEY}',
        'Content-Type': 'application/json'
    }
    data = {
        "model": "llama3-8b-8192",
        "messages": [{
            "role": "user",
            "content": f'{prompt}'
        }]
    }
    response = requests.post(GROQ_API_ENDPOINT, json=data, headers=headers)
    if response.status_code == 200:
        json_response = response.json()
        choices = json_response.get('choices', [])
        
        if choices:
            message_content = choices[0].get('message', {}).get('content', 'No content in AI response.')
            return message_content
        else:
            return 'No choices in AI response.'
    else:
        return f"Error: {response.status_code} - {response.text}"

def main() -> None:
    updater = Updater(TELEGRAM_BOT_TOKEN)

    dispatcher = updater.dispatcher

    dispatcher.add_handler(CommandHandler("start", start))
    dispatcher.add_handler(CommandHandler("student", student))
    dispatcher.add_handler(CommandHandler("it_tech", it_tech))
    dispatcher.add_handler(CommandHandler("contacts", contacts))
    dispatcher.add_handler(CommandHandler("ai_prompt", ai_prompt))
    dispatcher.add_handler(MessageHandler(Filters.text & ~Filters.command, handle_ai_prompt))

    updater.start_polling()

    updater.idle()

if __name__ == '__main__':
    main()
