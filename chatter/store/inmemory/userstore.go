package inmemory

import "github.com/npathai/chatter/model"

type MemoryUserStore struct {
	users []*model.User
}

func (store *MemoryUserStore) GetAllUsers() ([]*model.User, error) {
	if store.users == nil {
		return []*model.User{}, nil
	}
	return store.users, nil
}

func (store *MemoryUserStore) Save(user *model.User) (*model.User, error) {
	store.users = append(store.users, user)
	return user, nil
}

func (store *MemoryUserStore) Get(userId string) (*model.User, error) {
	return nil, nil
}