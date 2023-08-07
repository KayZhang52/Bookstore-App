import axios from 'axios';
import HttpService from "./htttp.service";

class BamazonService {
    // authEndpoint = process.env.API_URL;

    getBooks = async () => {
        const endpoint = 'api/book/getAll';
        return await HttpService.get(endpoint);
    };

    getImage = async (bookId) => {
        const headers = { "Content-Type": "application/vnd.api+json", "Accept": "application/vnd.api+json", 'Access-Control-Allow-Credentials': true }
        return await axios.get('api/book/image?bookId=' + bookId, { responseType: "blob", headers: headers })
    }
}

export default new BamazonService();
