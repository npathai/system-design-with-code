package commands

import (
	"github.com/npathai/chatter/api"
	"github.com/npathai/chatter/app"
	"github.com/spf13/cobra"
	"os"
	"os/signal"
	"syscall"
)

var serverCmd = &cobra.Command {
	Use: "server",
	Short: "Run the Chatter server",
	RunE: serverCommandFunc,
}

func init() {
	RootCmd.AddCommand(serverCmd)
	RootCmd.RunE = serverCommandFunc
}

func serverCommandFunc(command *cobra.Command, args []string) error {
	runServer()
	return nil
}

func runServer() {
	s, err := app.NewServer()
	if err != nil {
		panic("couldn't start server")
	}
	defer s.Stop()

	api.Init(s.AppOptions, s.Router)
	s.Start()

	ch := make(chan os.Signal)
	signal.Notify(ch, os.Interrupt, syscall.SIGINT, syscall.SIGTERM)

	// Block on channel read till there is interrupt/terminate signal
	<- ch
}
