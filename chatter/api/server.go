package api

import (
	"fmt"
	"net/http"
	"time"
)
import "github.com/gorilla/mux"

var Srv *Server

type Server struct {
	Router *mux.Router
	Server *http.Server
}

func NewServer() {
	Srv = &Server{}
	Srv.Router = mux.NewRouter()
}

func StartServer() {
	fmt.Println("Starting server")
	Srv.Server = &http.Server{
		Addr: "127.0.0.1:8080",
		Handler: Srv.Router,
		WriteTimeout: 15 * time.Second,
		ReadTimeout:  15 * time.Second,
	}

}

func StopServer() {
	fmt.Println("Server stopped")
}
