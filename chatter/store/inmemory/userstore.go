package inmemory

import "github.com/npathai/chatter/model"

type MemoryUserStore struct {
	users []*model.User
}

func (store *MemoryUserStore) Save(user *model.User) (*model.User, error) {
	store.users = append(store.users, user)
	return user, nil
}
