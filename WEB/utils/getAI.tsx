import axios from 'axios';
import OPENAI_API_KEY from '../config/openaiConfig';

let lastRequestTime = 0;

export const sendMessageToOpenAI = async (message: string): Promise<string> => {
  const currentTime = Date.now();

  if (currentTime - lastRequestTime < 1000) {
    throw new Error('Bạn đang gửi yêu cầu quá nhanh. Vui lòng chờ một chút.');
  }
  lastRequestTime = currentTime;
  try {
    const response = await axios.post(
        "https://api.openai.com/v1/chat/completions",
        {
          model: "gpt-3.5-turbo-16k",
          messages: [{ role: "user", content: message }],
        },
        {
          headers: {
            Authorization: `Bearer ${OPENAI_API_KEY}`,
            "Content-Type": "application/json",
          },
        }
      );

    return response.data.choices[0].message.content;
  } catch (error: any) {
    if (error.response?.status === 429) {
      return 'Bạn đã gửi quá nhiều yêu cầu. Vui lòng thử lại sau.';
    }
    throw new Error('Đã xảy ra lỗi khi kết nối với OpenAI.');
  }
};