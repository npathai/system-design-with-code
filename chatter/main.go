package main

import (
	"fmt"
	"github.com/npathai/chatter/api"
	"os"
	"os/signal"
	"syscall"
)

func main() {
	fmt.Println("Welcome to Chatter, the single place for all your communications")
	api.NewServer()
	api.InitApi()

	ch := make(chan os.Signal)
	signal.Notify(ch, os.Interrupt, syscall.SIGINT, syscall.SIGTERM)

	// Block on channel read
	<- ch
	api.StopServer()
}
