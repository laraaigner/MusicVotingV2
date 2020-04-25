import { sha256 } from "js-sha256";

export default {
    authenticate() {
        //@ts-ignore
        return fetch(process.env.VUE_APP_API_URL + '/auth/validate', {
            method: 'GET',
            credentials: 'include',
        })
            .then(res => {
                if (res.status == 200 && localStorage.getItem("isLoggedIn") != "true") {
                    localStorage.setItem("isLoggedIn", "true")
                }
                else if (res.status == 403) {
                    localStorage.setItem("isLoggedIn", "false")
                }
                return Promise.resolve(res)
            });
    },

    login(password: string) {
        // if he is already logged in don't log in again
        if (localStorage.getItem("isLoggedIn") == "true")
            return Promise.reject()
        //@ts-ignore
        return fetch(process.env.VUE_APP_API_URL + '/auth', {
            method: 'POST',
            headers: {
                "Content-Type": "text/plain",
            },
            credentials: 'include',
            body: sha256(password)
        })
            .then(res => {
                if (res.status == 200) {
                    localStorage.setItem("isLoggedIn", "true")
                }
                return Promise.resolve(res)
            });
    }
}