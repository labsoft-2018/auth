(ns auth.logic.facebook)

(defn fb-url-for-token [token]
  (str "https://graph.facebook.com/v2.9/me?fields=id%2Cemail%2Cname%2Cpicture{url}&access_token=" token))